package ifa.devlog.gestparc.controller;

import ifa.devlog.gestparc.dao.LocationDao;
import ifa.devlog.gestparc.model.Location;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin

public class LocationController {
    private LocationDao locationDao;

    @Autowired
    LocationController(
            LocationDao locationDao
    ) {
        this.locationDao = locationDao;
    }

    @GetMapping("/user/locations")
    public ResponseEntity<List<Location>> getLocations() {
        return ResponseEntity.ok(locationDao.findAll());
    }

    @GetMapping("/user/location/{id}")
    public ResponseEntity<Location> getLocation(@PathVariable int id) {
        Optional<Location> location = locationDao.findById(id);
        if (location.isPresent()) {
            return ResponseEntity.ok(location.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/user/location/new")
    public ResponseEntity<String> create(@RequestBody Location location) {
        locationDao.saveAndFlush(location);
            return ResponseEntity.ok(Integer.toString(location.getId()));
    }
    @PostMapping("/user/location/update")
    public ResponseEntity<String> update(@RequestBody Location location) {
        locationDao.saveAndFlush(location);
        return ResponseEntity.ok(Integer.toString(location.getId()));
    }

    @DeleteMapping("/user/location/delete/{id}")
    public ResponseEntity<Integer> deleteStatut(@PathVariable int id) {
        if (locationDao.existsById(id)) {
            locationDao.deleteById(id);
            return ResponseEntity.ok(id);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
