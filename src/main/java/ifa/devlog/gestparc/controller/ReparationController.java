package ifa.devlog.gestparc.controller;

import com.fasterxml.jackson.annotation.JsonView;
import ifa.devlog.gestparc.dao.HistoriqueDao;
import ifa.devlog.gestparc.dao.MaterielDao;
import ifa.devlog.gestparc.dao.ReparationDao;
import ifa.devlog.gestparc.model.Historique;
import ifa.devlog.gestparc.model.Materiel;
import ifa.devlog.gestparc.model.Reparation;
import ifa.devlog.gestparc.view.CustomJsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin

public class ReparationController {
    private ReparationDao reparationDao;
    private MaterielDao materielDao;
    private HistoriqueDao historiqueDao;

    @Autowired
    ReparationController(
            MaterielDao materielDao,
            ReparationDao reparationDao,
            HistoriqueDao historiqueDao
    ) {
        this.reparationDao = reparationDao;
        this.materielDao = materielDao;
        this.historiqueDao = historiqueDao;
    }

    @JsonView(CustomJsonView.VueReparation.class)
    @GetMapping("/admin/reparations")
    public ResponseEntity<List<Reparation>> getReparations() {
        return ResponseEntity.ok(reparationDao.findAll());
    }

    @JsonView(CustomJsonView.VueReparation.class)
    @GetMapping("/admin/reparations/encours")
    public ResponseEntity<List<Reparation>> getReparationsEncours() {
        return ResponseEntity.ok(reparationDao.findByEtatNot(Reparation.Finalisée));
    }

    @JsonView(CustomJsonView.VueReparation.class)
    @GetMapping("/admin/reparation/{id}")
    public ResponseEntity<Reparation> getReparation(@PathVariable int id) {
        Optional<Reparation> reparation = reparationDao.findById(id);
        if (reparation.isPresent()) {
            return ResponseEntity.ok(reparation.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }
    @JsonView(CustomJsonView.VueReparation.class)
    @PostMapping("/admin/reparation/new")
    public ResponseEntity<String> create(@RequestBody Reparation reparation) {
        reparation.setEtat(0);
        reparation.setDate_envoi(LocalDate.now());
        Optional<Materiel> optionalMateriel = materielDao.findById(reparation.getMateriel().getId());
        if (!optionalMateriel.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        Materiel materiel = optionalMateriel.get();
        reparation.setEtatEntrant(optionalMateriel.get().getEtat());
        materiel.setEtat(reparation.getEtatCasse());
        Reparation reparationCreated = reparationDao.saveAndFlush(reparation);
        Historique historique = new Historique();
        historique.setDate(LocalDate.now());
        historique.setReparation(reparationCreated);
        historique.setMateriel(materiel);
        historique.setEtat(materiel.getEtat());
        historiqueDao.saveAndFlush(historique);
        materielDao.saveAndFlush(materiel);

        return ResponseEntity.ok(Integer.toString(reparation.getId()));
    }

    @JsonView(CustomJsonView.VueReparation.class)
    @PostMapping("/admin/reparation/update")
    public ResponseEntity<String> update(@RequestBody Reparation reparation) {
        Optional<Reparation> optionalReparation = reparationDao.findById(reparation.getId());
        if (!optionalReparation.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        if (optionalReparation.get().getEtat()==1) {
            return ResponseEntity.noContent().build();
        }
        System.out.println("********************************");
        Optional<Materiel> optionalMateriel = materielDao.findById(optionalReparation.get().getMateriel().getId());
        if (!optionalMateriel.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        System.out.println("********************************");
        reparation.setMateriel(optionalReparation.get().getMateriel());
        reparation.setDate_envoi(optionalReparation.get().getDate_envoi());
        reparation.setEtatEntrant(optionalReparation.get().getEtatEntrant());
        reparation.setEtatCasse(optionalReparation.get().getEtatCasse());
        if (reparation.getEtat()==1) {
            Materiel materiel = optionalMateriel.get();
            materiel.setEtat(reparation.getEtatRepare());
            Historique historique = new Historique();
            historique.setDate(LocalDate.now());
            historique.setReparation(reparation);
            historique.setMateriel(materiel);
            historique.setEtat(materiel.getEtat());
            historiqueDao.saveAndFlush(historique);
            materielDao.saveAndFlush(materiel);
        }
        reparationDao.saveAndFlush(reparation);
        return ResponseEntity.ok(Integer.toString(reparation.getId()));
    }

}