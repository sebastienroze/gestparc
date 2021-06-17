package ifa.devlog.gestparc.controller;

import ifa.devlog.gestparc.dao.UtilisateurDao;
import ifa.devlog.gestparc.model.Role;
import ifa.devlog.gestparc.model.Utilisateur;
import ifa.devlog.gestparc.security.JwtUtil;
import ifa.devlog.gestparc.security.UserDetailsCustom;
import ifa.devlog.gestparc.security.UserDetailsServiceCustom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.util.List;
import java.util.Optional;

@RestController
@CrossOrigin


public class UtilisateurController {
    private PasswordEncoder passwordEncoder;
    private JwtUtil jwtUtil;
    private AuthenticationManager authenticationManager;
    private UserDetailsServiceCustom userDetailsServiceCustom;
    private UtilisateurDao utilisateurDao;
    @Value("${message.erreur}")
    private String messageErreur;


    @Autowired
    UtilisateurController(
            UtilisateurDao utilisateurDao,
            PasswordEncoder passwordEncoder,
            JwtUtil jwtUtil,
            AuthenticationManager authenticationManager,
            UserDetailsServiceCustom userDetailsServiceCustom
            ) {
        this.utilisateurDao = utilisateurDao;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager =  authenticationManager;
        this.userDetailsServiceCustom = userDetailsServiceCustom;

    }

    @GetMapping("/test/test")
    public ResponseEntity<String> getTest() {
        String str = "";

        File fileName = new File("/home/doc/test");
        str = fileName.getAbsolutePath();
        /*
        File[] fileList = fileName.listFiles();
        String str = "";
        for (File file: fileList) {
            str = str +file + "\n";
        }
*/
        return ResponseEntity.ok(str);
    }

    @GetMapping("/admin/utilisateurs")
    public ResponseEntity<List<Utilisateur>> getUtilisateurs() {
        return ResponseEntity.ok(utilisateurDao.findAll());
    }

    @GetMapping("/admin/utilisateur/{id}")
    public ResponseEntity<Utilisateur> getUtilisateur(@PathVariable int id) {
        Optional<Utilisateur> utilisateur = utilisateurDao.findById(id);
        if (utilisateur.isPresent()) {
            return ResponseEntity.ok(utilisateur.get());
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/admin/utilisateur/new")
    public ResponseEntity<String> inscription(@RequestBody Utilisateur utilisateur) {
        Optional<Utilisateur> utilisateurDoublon=  utilisateurDao.findByLogin(utilisateur.getLogin());
        if (utilisateurDoublon.isPresent()) {
            return ResponseEntity.badRequest().body("Ce login est déjà utilisé");
        } else {
            utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));
            utilisateurDao.saveAndFlush(utilisateur);
            return ResponseEntity.ok(Integer.toString(utilisateur.getId()));
        }
    }
    @PostMapping("/admin/utilisateur/update")
    public ResponseEntity<String> update(@RequestBody Utilisateur utilisateur) {
        if ("".equals(utilisateur.getPassword())) {
            Optional<Utilisateur> utilisateurDoublon=  utilisateurDao.findById(utilisateur.getId());
            if (utilisateurDoublon.isPresent()) {
                utilisateur.setPassword(utilisateurDoublon.get().getPassword());
            } else {
                return ResponseEntity.badRequest().body("Login inexistant");
            }
        } else {
            utilisateur.setPassword(passwordEncoder.encode(utilisateur.getPassword()));
        }
        utilisateurDao.saveAndFlush(utilisateur);
        return ResponseEntity.ok(Integer.toString(utilisateur.getId()));
    }
    @DeleteMapping("/admin/utilisateur/delete/{id}")
    public ResponseEntity<Integer> deleteStatut(@PathVariable int id) {
        System.out.println("delete");
        if (utilisateurDao.existsById(id)) {
            utilisateurDao.deleteById(id);
            return ResponseEntity.ok(id);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @PostMapping("/authentication")
    public ResponseEntity<String> authentication(@RequestBody Utilisateur utilisateur) {
        System.out.println("****authentification****");
        System.out.println(utilisateur.getLogin());
        System.out.println(utilisateur.getPassword());
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            utilisateur.getLogin(), utilisateur.getPassword()
                    )
            );
        } catch(AuthenticationException e) {
            return ResponseEntity.badRequest().body(messageErreur);
        }
        UserDetailsCustom userDetails = this.userDetailsServiceCustom.loadUserByUsername(utilisateur.getLogin());
        return ResponseEntity.ok("{\"bearer\":\""+jwtUtil.generateToken(userDetails)
                                +"\",\"roles\":\""+userDetails.getRoles().toString()   +"\"}");
    }
}
