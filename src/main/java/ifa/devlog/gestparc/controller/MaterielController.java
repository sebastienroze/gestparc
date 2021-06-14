package ifa.devlog.gestparc.controller;

import com.fasterxml.jackson.annotation.JsonView;
import ifa.devlog.gestparc.dao.LocationDao;
import ifa.devlog.gestparc.dao.MaterielDao;
import ifa.devlog.gestparc.dao.ReparationDao;
import ifa.devlog.gestparc.dao.TypeMaterielDao;
import ifa.devlog.gestparc.model.Location;
import ifa.devlog.gestparc.model.Materiel;
import ifa.devlog.gestparc.model.Reparation;
import ifa.devlog.gestparc.model.TypeMateriel;
import ifa.devlog.gestparc.more.Bordereau;
import ifa.devlog.gestparc.more.EtatStock;
import ifa.devlog.gestparc.more.MaterielDetail;
import ifa.devlog.gestparc.view.CustomJsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin

public class MaterielController {
    private MaterielDao materielDao;
    private TypeMaterielDao typeMaterielDao;
    private LocationDao locationDao;
    private ReparationDao reparationDao;

    @Autowired
    MaterielController(
            MaterielDao materielDao,
            TypeMaterielDao typeMaterielDao,
            LocationDao locationDao,
            ReparationDao reparationDao
    ) {
        this.materielDao = materielDao;
        this.typeMaterielDao = typeMaterielDao;
        this.locationDao = locationDao;
        this.reparationDao = reparationDao;
    }

    @JsonView(CustomJsonView.VueMateriel.class)
    @GetMapping("/admin/materiels")
    public ResponseEntity<List<Materiel>> getMateriels() {
        return ResponseEntity.ok(materielDao.findAll());
    }

    public List<Materiel> getMaterielsListForLocation(Location location) {
        List<Materiel>  materiels = materielDao.findByTypeMateriel(location.getTypeMateriel());
        List<Location> locations = locationDao.findByEtatOrEtat(Location.EnPret,Location.Valide);
        List<Reparation> reparations = reparationDao.findByEtat(0);
        materiels.removeIf(materiel -> {
            for (Location autreLocation:locations) {
                if (autreLocation.getMateriel().getId()==materiel.getId()) {
                    if (!((location.getDate_debut().isBefore(autreLocation.getDate_debut())
                            && location.getDate_retour().isBefore(autreLocation.getDate_debut())
                    ) || (
                            location.getDate_debut().isAfter(autreLocation.getDate_retour())
                                    && location.getDate_retour().isAfter(autreLocation.getDate_retour())
                    )
                    )) {
                        return true;
                    }
                }
            }
            for (Reparation reparation:reparations) {
                if (reparation.getMateriel().getId()==materiel.getId()) {
                    if ((reparation.getDate_retour()==null) ||
                            location.getDate_debut().isBefore(reparation.getDate_retour())) {
                        return true;
                    }
                }
            }
            return false;
        });
        return materiels;
    }

    @GetMapping("/admin/materielsForLocation/{id}")
    public ResponseEntity<List<Materiel>> getMaterielsForLocation(@PathVariable int id) {
        TypeMateriel typeMateriel = new TypeMateriel();
        typeMateriel.setId(id);
        Optional<Location> optionalLocation = locationDao.findById(id);
        if (optionalLocation.isPresent()) {
            Location location = optionalLocation.get();
            return ResponseEntity.ok(getMaterielsListForLocation(location));
        }
        return ResponseEntity.noContent().build();
    }

    @JsonView(CustomJsonView.VueMateriel.class)
    @GetMapping("/user/materiel/{id}")
    public ResponseEntity<Materiel> getMateriel(@PathVariable int id) {
        Optional<Materiel> optionalMateriel = materielDao.findById(id);
        if (optionalMateriel.isPresent()) {
            Materiel materiel = optionalMateriel.get();
            return ResponseEntity.ok(materiel);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @JsonView({CustomJsonView.VueMaterielDetail.class})
    @GetMapping("/admin/materielDetail/{id}")
    public ResponseEntity<MaterielDetail> getMaterielDetail(@PathVariable int id) {
        Optional<Materiel> optionalMateriel = materielDao.findById(id);
        if (optionalMateriel.isPresent()) {
            MaterielDetail materielDetail = new MaterielDetail();
            Materiel materiel = optionalMateriel.get();
            materielDetail.setMateriel(materiel);

            List<Location> locations = locationDao.findByMaterielAndEtat(materiel,Location.EnPret);
            if (locations.size()>0) {
                materielDetail.setLocation(locations.get(0));
            }

            List<Reparation> reparations = reparationDao.findByMaterielAndEtat(materiel,0);
            if (reparations.size()>0) {
                materielDetail.setReparation(reparations.get(0));
            }
            return ResponseEntity.ok(materielDetail);
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

    @GetMapping("/docs/materiel/etatStock")
    public ResponseEntity<byte[]> getEtatStock() {
        String documentName = "Etat du stock " + LocalDate.now() + ".pdf";
        File file = new File(documentName);
        byte[] contents = new byte[0];
//        if (!file.exists())
        {
               EtatStock etatStock = new EtatStock();
            etatStock.generate(materielDao,locationDao,reparationDao);
        }
        try {
            contents = Files.readAllBytes(file.toPath());
        } catch (IOException e) {e.printStackTrace();}

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }
}