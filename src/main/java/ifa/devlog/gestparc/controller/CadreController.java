package ifa.devlog.gestparc.controller;

import ifa.devlog.gestparc.dao.CadreDao;
import ifa.devlog.gestparc.model.Cadre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin

public class CadreController {
    private CadreDao cadreDao;

    @Autowired
    CadreController(
            CadreDao cadreDao
    ) {
        this.cadreDao = cadreDao;
    }

    @GetMapping("/user/cadres")
    public ResponseEntity<List<Cadre>> getCadres() {
        return ResponseEntity.ok(cadreDao.findAll());
    }

    @GetMapping("/user/cadre/{id}")
    public ResponseEntity<Cadre> getCadre(@PathVariable int id) {
        Optional<Cadre> cadre = cadreDao.findById(id);
        if (cadre.isPresent()) {
            return ResponseEntity.ok(cadre.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/admin/cadre/new")
    public ResponseEntity<String> create(@RequestBody Cadre cadre) {
            cadreDao.saveAndFlush(cadre);
            return ResponseEntity.ok(Integer.toString(cadre.getId()));
    }
    @PostMapping("/admin/cadre/update")
    public ResponseEntity<String> update(@RequestBody Cadre cadre) {
        cadreDao.saveAndFlush(cadre);
        return ResponseEntity.ok(Integer.toString(cadre.getId()));
    }

    @DeleteMapping("/admin/cadre/delete/{id}")
    public ResponseEntity<Integer> deleteStatut(@PathVariable int id) {
        if (cadreDao.existsById(id)) {
            cadreDao.deleteById(id);
            return ResponseEntity.ok(id);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}