package ifa.devlog.gestparc.controller;

import ifa.devlog.gestparc.dao.TypeMaterielDao;
import ifa.devlog.gestparc.model.TypeMateriel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin

public class TypeMaterielController {
    private TypeMaterielDao typeMaterielDao;

    @Autowired
    TypeMaterielController(
            TypeMaterielDao typeMaterielDao
    ) {
        this.typeMaterielDao = typeMaterielDao;
    }

    @GetMapping("/user/typeMateriels")
    public ResponseEntity<List<TypeMateriel>> getTypeMateriels() {
        return ResponseEntity.ok(typeMaterielDao.findAll());
    }

    @GetMapping("/user/typeMateriel/{id}")
    public ResponseEntity<TypeMateriel> getTypeMateriel(@PathVariable int id) {
        Optional<TypeMateriel> typeMateriel = typeMaterielDao.findById(id);
        if (typeMateriel.isPresent()) {
            return ResponseEntity.ok(typeMateriel.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/admin/typeMateriel/new")
    public ResponseEntity<String> inscription(@RequestBody TypeMateriel typeMateriel) {
            typeMaterielDao.saveAndFlush(typeMateriel);
            return ResponseEntity.ok(Integer.toString(typeMateriel.getId()));
    }
    @PostMapping("/admin/typeMateriel/update")
    public ResponseEntity<String> update(@RequestBody TypeMateriel typeMateriel) {
        typeMaterielDao.saveAndFlush(typeMateriel);
        return ResponseEntity.ok(Integer.toString(typeMateriel.getId()));
    }

    @DeleteMapping("/admin/typeMateriel/delete/{id}")
    public ResponseEntity<Integer> deleteStatut(@PathVariable int id) {
        if (typeMaterielDao.existsById(id)) {
            typeMaterielDao.deleteById(id);
            return ResponseEntity.ok(id);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}