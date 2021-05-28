package ifa.devlog.gestparc.security;

import ifa.devlog.gestparc.dao.UtilisateurDao;
import ifa.devlog.gestparc.model.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceCustom implements UserDetailsService {
    @Autowired
    UtilisateurDao utilisateurDao;

    @Override
    public UserDetailsCustom loadUserByUsername(String s) throws UsernameNotFoundException {

        Utilisateur utilisateur = utilisateurDao
                .findByLogin(s)
                .orElseThrow(()-> new UsernameNotFoundException(s + " inconnu"));
        return new UserDetailsCustom(utilisateur);
    }
}
