package ifa.devlog.gestparc.controller;

import ifa.devlog.gestparc.dao.MaterielDao;
import ifa.devlog.gestparc.model.Materiel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin

public class MaterielController {
    private MaterielDao materielDao;

    @Autowired
    MaterielController(
            MaterielDao materielDao
    ) {
        this.materielDao = materielDao;
    }

    @GetMapping("/user/materiels")
    public ResponseEntity<List<Materiel>> getMateriels() {
        return ResponseEntity.ok(materielDao.findAll());
    }

    @GetMapping("/user/materiel/{id}")
    public ResponseEntity<Materiel> getMateriel(@PathVariable int id) {
        Optional<Materiel> materiel = materielDao.findById(id);
        if (materiel.isPresent()) {
            return ResponseEntity.ok(materiel.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/admin/materiel/new")
    public ResponseEntity<String> inscription(@RequestBody Materiel materiel) {
            materielDao.saveAndFlush(materiel);
            return ResponseEntity.ok(Integer.toString(materiel.getId()));
    }
    @PostMapping("/admin/materiel/update")
    public ResponseEntity<String> update(@RequestBody Materiel materiel) {
        materielDao.saveAndFlush(materiel);
        return ResponseEntity.ok(Integer.toString(materiel.getId()));
    }

    @DeleteMapping("/admin/materiel/delete/{id}")
    public ResponseEntity<Integer> deleteStatut(@PathVariable int id) {
        if (materielDao.existsById(id)) {
            materielDao.deleteById(id);
            return ResponseEntity.ok(id);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}