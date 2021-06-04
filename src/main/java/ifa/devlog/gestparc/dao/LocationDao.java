package ifa.devlog.gestparc.dao;

import ifa.devlog.gestparc.model.Location;
import ifa.devlog.gestparc.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationDao extends JpaRepository<Location,Integer> {
    public List<Location> findByUtilisateur(Utilisateur utilisateur) ;
//    public Optional<List<Location>> findByUtilisateur(Utilisateur utilisateur) ;
}



