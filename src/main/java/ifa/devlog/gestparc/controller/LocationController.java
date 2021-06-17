package ifa.devlog.gestparc.controller;

import com.fasterxml.jackson.annotation.JsonView;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import ifa.devlog.gestparc.dao.HistoriqueDao;
import ifa.devlog.gestparc.dao.LocationDao;
import ifa.devlog.gestparc.model.Historique;
import ifa.devlog.gestparc.model.Location;
import ifa.devlog.gestparc.model.Materiel;
import ifa.devlog.gestparc.model.Utilisateur;
import ifa.devlog.gestparc.more.Bordereau;
import ifa.devlog.gestparc.security.JwtUtil;
import ifa.devlog.gestparc.view.CustomJsonView;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.*;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin

public class LocationController {
    private HistoriqueDao historiqueDao;
    private LocationDao locationDao;
    private JwtUtil jwtUtil;
    private MaterielController materielController;

    @Autowired
    LocationController(
            LocationDao locationDao,
            MaterielController materielController,
            JwtUtil jwtUtil,
            HistoriqueDao historiqueDao
    ) {
        this.locationDao = locationDao;
        this.materielController = materielController;
        this.jwtUtil = jwtUtil;
        this.historiqueDao = historiqueDao;
    }

    @JsonView(CustomJsonView.VueLocation.class)
    @GetMapping("/user/locations")
    public ResponseEntity<List<Location>> getLocations(@RequestHeader(value="Authorization") String authorization) {
        boolean isAdmin = jwtUtil.getIsAdminFromAuthorization(authorization);
        if (isAdmin) return ResponseEntity.ok(locationDao.findAll());
        Integer idUtilisateur = jwtUtil.getUtilisateurIdFromAuthorization(authorization);
        Utilisateur utilisateur = new Utilisateur(idUtilisateur);
        return ResponseEntity.ok(locationDao.findByUtilisateur(utilisateur));
    }

    @GetMapping("/user/locations/encours")
    public ResponseEntity<List<Location>> getLocationsEncours(@RequestHeader(value="Authorization") String authorization) {
        boolean isAdmin = jwtUtil.getIsAdminFromAuthorization(authorization);
        if (isAdmin) return ResponseEntity.ok(locationDao.findByEtatNot(Location.Finalisée));
        Integer idUtilisateur = jwtUtil.getUtilisateurIdFromAuthorization(authorization);
        Utilisateur utilisateur = new Utilisateur(idUtilisateur);
        return ResponseEntity.ok(locationDao.findByUtilisateurAndEtatNot(utilisateur,Location.Finalisée));
    }

    @JsonView(CustomJsonView.VueLocation.class)
    @GetMapping("/admin/locationsEnPret")
    public ResponseEntity<List<Location>> getLocationsEnPret() {
          return ResponseEntity.ok(locationDao.findByEtat(Location.EnPret));
    }

    @JsonView(CustomJsonView.VueLocation.class)
    @GetMapping("/user/location/{id}")
    public ResponseEntity<Location> getLocation(@PathVariable int id,
            @RequestHeader(value="Authorization") String authorization) {
        Integer idUtilisateur = jwtUtil.getUtilisateurIdFromAuthorization(authorization);
        boolean isAdmin = jwtUtil.getIsAdminFromAuthorization(authorization);
        Optional<Location> location = locationDao.findById(id);
        if (location.isPresent()) {
            if (location.get().getUtilisateur().getId() == idUtilisateur || isAdmin) {
                return ResponseEntity.ok(location.get());
            }
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
        }
        return ResponseEntity.noContent().build();
    }

//    @GetMapping("/admin/location/borderau/{id}")
    @GetMapping("/docs/location/borderau/{id}")
    public ResponseEntity<byte[]> getBorderau(@PathVariable int id
    ) {
        String documentName = "/home/doc/";
        documentName = documentName+"Bordereau " + id + ".pdf";
        File file = new File(documentName);
        byte[] contents = new byte[0];
        if (!file.exists()) {
                Optional<Location> optionalLocation = locationDao.findById(id);
                if (optionalLocation.isPresent()) {
                    if (optionalLocation.get().getEtat()<Location.EnPret) {
                        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                    }
                    Bordereau bordereau = new Bordereau(optionalLocation.get());
                    bordereau.generate(documentName);
                }
        }
        try {
            contents = Files.readAllBytes(file.toPath());
        } catch (IOException e) {e.printStackTrace();}

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
//        headers.set("X-Frame-Options","SAMEORIGIN");
        return new ResponseEntity<>(contents, headers, HttpStatus.OK);
    }

    @PostMapping("/user/location/new")
    public ResponseEntity<String> create(
            @RequestBody Location location,
            @RequestHeader(value="Authorization") String authorization) {
        Integer idUtilisateur = jwtUtil.getUtilisateurIdFromAuthorization(authorization);
        location.setUtilisateur(new Utilisateur(idUtilisateur));
        location.setEtat(0);
        location.setMateriel(null);
        locationDao.saveAndFlush(location);
            return ResponseEntity.ok(Integer.toString(location.getId()));
    }

    @PostMapping("/user/location/update")
    public ResponseEntity<String> update(@RequestBody Location location,
                                         @RequestHeader(value="Authorization") String authorization ) {
        Integer idUtilisateur = jwtUtil.getUtilisateurIdFromAuthorization(authorization);
        System.out.println("****************************************");
        System.out.println(location);
        Optional<Location> locationDoublon =  locationDao.findById(location.getId());
        if (locationDoublon.isPresent()) {
            if (locationDoublon.get().getUtilisateur().getId() == idUtilisateur &&
                !locationDoublon.get().getValide() ) {
                location.setUtilisateur(locationDoublon.get().getUtilisateur());
                location.setEtat(0);
                location.setMateriel(null);
                locationDao.saveAndFlush(location);
                return ResponseEntity.ok(Integer.toString(location.getId()));
            } else {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
            }
        }
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/admin/location/update")
    public ResponseEntity<String> update(@RequestBody Location location) {
        Optional<Location> locationDoublon =  locationDao.findById(location.getId());
        if (locationDoublon.isPresent()) {
            if ((locationDoublon.get().getEtat()<=Location.Valide) &&
                    ( (location.getEtat()<=Location.Valide ) ||
                      (location.getEtat()<=Location.EnPret )
                    )) {
                location.setUtilisateur(locationDoublon.get().getUtilisateur());
                location.setTypeMateriel(locationDoublon.get().getTypeMateriel());
                location.setDate_debut(locationDoublon.get().getDate_debut());
                location.setDate_retour(locationDoublon.get().getDate_retour());
                location.setCadre(locationDoublon.get().getCadre()); ;
                if ((locationDoublon.get().getEtat() == Location.Demande) &&
                        location.getEtat() ==Location.Valide){
                    // validation de la location
                    List<Materiel> listMateriels = this.materielController.getMaterielsListForLocation(location);
                    if (!(listMateriels.stream().anyMatch(materiel -> materiel.getId()== location.getMateriel().getId()))) {
                        return ResponseEntity.noContent().build();
                    }
                } else if ((locationDoublon.get().getEtat() == Location.Valide) &&
                         location.getEtat() ==Location.EnPret) {
                    // Pret de la location
                    location.setMateriel(locationDoublon.get().getMateriel());
                    Historique historique = new Historique();
                    historique.setDate(LocalDate.now());
                    historique.setLocation(location);
                    historique.setMateriel(location.getMateriel());
                    historique.setEtat(locationDoublon.get().getMateriel().getEtat());
                    this.historiqueDao.saveAndFlush(historique);
                }
                locationDao.saveAndFlush(location);
                return ResponseEntity.ok(Integer.toString(location.getId()));
            }
        }
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/user/location/delete/{id}")
    public ResponseEntity<Integer> deleteStatut(@PathVariable int id,
            @RequestHeader(value="Authorization") String authorization) {
        Integer idUtilisateur = jwtUtil.getUtilisateurIdFromAuthorization(authorization);
        if (locationDao.existsById(id)) {
            Optional<Location> location =  locationDao.findById(id);
            if (location.isPresent()) {
                if (location.get().getUtilisateur().getId() == idUtilisateur &&
                    !location.get().getValide()  ) {
                    locationDao.deleteById(id);
                    System.out.println("delete ok");
                    return ResponseEntity.ok(id);

                } else {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(null);
                }
            } else {
                return ResponseEntity.noContent().build();
            }
        } else {
            return ResponseEntity.noContent().build();
        }
    }


}
