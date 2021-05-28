package ifa.devlog.gestparc.controller;

import ifa.devlog.gestparc.dao.RoleDao;
import ifa.devlog.gestparc.model.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin

public class RoleController {
    private RoleDao roleDao;

    @Autowired
    RoleController(
            RoleDao roleDao
    ) {
        this.roleDao = roleDao;
    }

    @GetMapping("/admin/roles")
    public ResponseEntity<List<Role>> getRoles() {
        return ResponseEntity.ok(roleDao.findAll());
    }

}