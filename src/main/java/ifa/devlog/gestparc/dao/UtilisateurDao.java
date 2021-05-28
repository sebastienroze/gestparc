package ifa.devlog.gestparc.dao;

import ifa.devlog.gestparc.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurDao  extends JpaRepository<Utilisateur,Integer> {
    public Optional<Utilisateur> findByLogin(String login) ;
}
