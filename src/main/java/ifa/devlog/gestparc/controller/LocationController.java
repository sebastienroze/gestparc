package ifa.devlog.gestparc.controller;

import ifa.devlog.gestparc.dao.LocationDao;
import ifa.devlog.gestparc.model.Location;
import ifa.devlog.gestparc.model.Utilisateur;
import ifa.devlog.gestparc.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin

public class LocationController {
    private LocationDao locationDao;
    private JwtUtil jwtUtil;

    @Autowired
    LocationController(
            LocationDao locationDao,
            JwtUtil jwtUtil
    ) {
        this.locationDao = locationDao;
        this.jwtUtil = jwtUtil;

    }

    @GetMapping("/user/locations")
    public ResponseEntity<List<Location>> getLocations(@RequestHeader(value="Authorization") String authorization) {
        boolean isAdmin = jwtUtil.getIsAdminFromAuthorization(authorization);
        if (isAdmin) return ResponseEntity.ok(locationDao.findAll());
        Integer idUtilisateur = jwtUtil.getUtilisateurIdFromAuthorization(authorization);
        Utilisateur utilisateur = new Utilisateur(idUtilisateur);
        return ResponseEntity.ok(locationDao.findByUtilisateur(utilisateur));
    }

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

    @PostMapping("/user/location/new")
    public ResponseEntity<String> create(
            @RequestBody Location location,
            @RequestHeader(value="Authorization") String authorization) {
        Integer idUtilisateur = jwtUtil.getUtilisateurIdFromAuthorization(authorization);
        location.setUtilisateur(new Utilisateur(idUtilisateur));
        location.setValide(false);
        location.setMateriel(null);
        locationDao.saveAndFlush(location);
            return ResponseEntity.ok(Integer.toString(location.getId()));
    }

    @PostMapping("/user/location/update")
    public ResponseEntity<String> update(@RequestBody Location location,
                                         @RequestHeader(value="Authorization") String authorization ) {
        Integer idUtilisateur = jwtUtil.getUtilisateurIdFromAuthorization(authorization);
        Optional<Location> locationDoublon =  locationDao.findById(location.getId());
        System.out.println(locationDoublon.get().getUtilisateur().getId() + " / " + idUtilisateur);
        if (locationDoublon.isPresent()) {
            if (locationDoublon.get().getUtilisateur().getId() == idUtilisateur &&
                locationDoublon.get().getValide() == false ) {
                location.setUtilisateur(locationDoublon.get().getUtilisateur());
                location.setValide(false);
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
            location.setUtilisateur(locationDoublon.get().getUtilisateur());
            location.setTypeMateriel(locationDoublon.get().getTypeMateriel());
            location.setDate_debut(locationDoublon.get().getDate_debut());
            location.setDate_retour(locationDoublon.get().getDate_retour());
            locationDao.saveAndFlush(location);
            return ResponseEntity.ok(Integer.toString(location.getId()));
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
                    location.get().getValide() == false ) {
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
