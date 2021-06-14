package ifa.devlog.gestparc.controller;

import com.fasterxml.jackson.annotation.JsonView;
import ifa.devlog.gestparc.dao.LocationDao;
import ifa.devlog.gestparc.dao.MaterielDao;
import ifa.devlog.gestparc.dao.ReparationDao;
import ifa.devlog.gestparc.model.Cadre;
import ifa.devlog.gestparc.model.Location;
import ifa.devlog.gestparc.model.Reparation;
import ifa.devlog.gestparc.more.Alerte;
import ifa.devlog.gestparc.view.CustomJsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin

public class AlerteController {
    private LocationDao locationDao;
    private ReparationDao reparationDao;
    @Autowired
    public AlerteController(
            LocationDao locationDao,
            ReparationDao reparationDao) {
        this.locationDao = locationDao;
        this.reparationDao = reparationDao;
    }

    @JsonView(CustomJsonView.VueAlerte.class)
    @GetMapping("/admin/alertes")
    public ResponseEntity<List<Alerte>> getAlertes() {
        List<Alerte> alertes = new ArrayList<Alerte>() ;
        List<Location> locations = locationDao.findByEtatNot(Location.Finalisée);
        for (Location location : locations) {
            switch (location.getEtat()) {
                case Location.Demande: {
                    alertes.add(new Alerte("Nouvelle réservation pour le "+location.getDate_debut(),location,null));
                    break;
                }
                case Location.Valide: {
                    List<Reparation> reparations = reparationDao.findByMaterielAndEtat(location.getMateriel(),Reparation.Demande);
                    reparations.removeIf(reparation -> {
                        if (reparation.getDate_retour()==null) return false;
                        if (!LocalDate.now().isBefore(location.getDate_debut())) return false;
                        if (reparation.getDate_retour().isBefore(location.getDate_debut())) return true;
                        return false;
                    });
                    if (reparations.size()!=0) {
                        alertes.add(new Alerte("Matériel réservé en réparation",location,reparations.get(0)));
                    }
                    break;
                }
                case Location.EnPret: {
                    if (!LocalDate.now().isBefore(location.getDate_retour())) {
                        alertes.add(new Alerte("Retour de stock "+location.getDate_retour(),location,null));
                    }
                    break;
                }
            }
        }
        List<Reparation> reparations = reparationDao.findByEtat(Reparation.Demande);
        for (Reparation reparation : reparations) {
            if (reparation.getDate_retour() == null) {
                alertes.add(new Alerte("Réparation sans date de fin",null,reparation));
            } else if (!LocalDate.now().isBefore(reparation.getDate_retour())) {
                alertes.add(new Alerte("Retour de stock "+reparation.getDate_retour(),null,reparation));
            }
        }
        return ResponseEntity.ok(alertes);
    }
}
