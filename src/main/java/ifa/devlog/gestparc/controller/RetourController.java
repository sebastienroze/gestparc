package ifa.devlog.gestparc.controller;

import com.fasterxml.jackson.annotation.JsonView;
import ifa.devlog.gestparc.dao.HistoriqueDao;
import ifa.devlog.gestparc.dao.LocationDao;
import ifa.devlog.gestparc.dao.MaterielDao;
import ifa.devlog.gestparc.dao.RetourDao;
import ifa.devlog.gestparc.model.Historique;
import ifa.devlog.gestparc.model.Location;
import ifa.devlog.gestparc.model.Materiel;
import ifa.devlog.gestparc.model.Retour;
import ifa.devlog.gestparc.view.CustomJsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin

public class RetourController {
    private HistoriqueDao historiqueDao;
    private RetourDao retourDao;
    private LocationDao loationDao;
    private MaterielDao materielDao;

    @Autowired
    RetourController(
            RetourDao retourDao,
            LocationDao loationDao,
            MaterielDao materielDao,
            HistoriqueDao historiqueDao
    ) {
        this.retourDao = retourDao;
        this.loationDao=loationDao;
        this.materielDao=materielDao;
        this.historiqueDao = historiqueDao;
    }
    @JsonView(CustomJsonView.VueRetour.class)
    @GetMapping("/admin/retours")
    public ResponseEntity<List<Retour>> getRetours() {
        return ResponseEntity.ok(retourDao.findAll());
    }

    @JsonView(CustomJsonView.VueRetour.class)
    @GetMapping("/admin/retour/{id}")
    public ResponseEntity<Retour> getRetour(@PathVariable int id) {
        Optional<Retour> retour = retourDao.findById(id);
        if (retour.isPresent()) {
            return ResponseEntity.ok(retour.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/admin/retour/new")
    public ResponseEntity<String> create(@RequestBody Retour retour) {
        Optional<Location> location = loationDao.findById(retour.getLocationId());
        if (!location.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        if (location.get().getEtat()!=Location.EnPret) {
            return ResponseEntity.noContent().build();
        }
        Materiel materiel = location.get().getMateriel();
        if(materiel ==null) {
            return ResponseEntity.noContent().build();
        }
        retour.setEtatEntrant(materiel.getEtat());
        location.get().setEtat(Location.Finalisée);
        materiel.setEtat(retour.getEtatSortant());
        Retour retourCreated = retourDao.saveAndFlush(retour);
        Historique historique = new Historique();
        historique.setDate(LocalDate.now());
        historique.setRetour(retourCreated);
        historique.setMateriel(materiel);
        historique.setEtat(materiel.getEtat());
        this.historiqueDao.saveAndFlush(historique);
        loationDao.saveAndFlush(location.get());
        materielDao.saveAndFlush(materiel);
        return ResponseEntity.ok(Integer.toString(retour.getId()));
    }

/*
    @PostMapping("/admin/retour/update")
    public ResponseEntity<String> update(@RequestBody Retour retour) {
        retourDao.saveAndFlush(retour);
        return ResponseEntity.ok(Integer.toString(retour.getId()));
    }
    */
/*
    @DeleteMapping("/admin/retour/delete/{id}")
    public ResponseEntity<Integer> deleteStatut(@PathVariable int id) {
        if (retourDao.existsById(id)) {
            retourDao.deleteById(id);
            return ResponseEntity.ok(id);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
*/
}