package ifa.devlog.gestparc.controller;

import com.fasterxml.jackson.annotation.JsonView;
import ifa.devlog.gestparc.dao.HistoriqueDao;
import ifa.devlog.gestparc.model.Historique;
import ifa.devlog.gestparc.model.Materiel;
import ifa.devlog.gestparc.view.CustomJsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin

public class HistoriqueController {
    private HistoriqueDao historiqueDao;

    @Autowired
    HistoriqueController(
            HistoriqueDao historiqueDao
    ) {
        this.historiqueDao = historiqueDao;
    }

    @JsonView(CustomJsonView.VueHistorique.class)
    @GetMapping("/admin/historiques/{idMateriel}")
    public ResponseEntity<List<Historique>> getHistoriques(@PathVariable int idMateriel) {
        return ResponseEntity.ok(historiqueDao.findByMateriel(new Materiel(idMateriel)));
    }

    @JsonView(CustomJsonView.VueHistorique.class)
    @GetMapping("/admin/historique/{id}")
    public ResponseEntity<Historique> getHistorique(@PathVariable int id) {
        Optional<Historique> historique = historiqueDao.findById(id);
        if (historique.isPresent()) {
            return ResponseEntity.ok(historique.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

}