package ifa.devlog.gestparc.dao;

import ifa.devlog.gestparc.model.Location;
import ifa.devlog.gestparc.model.Materiel;
import ifa.devlog.gestparc.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LocationDao extends JpaRepository<Location,Integer> {
    public List<Location> findByUtilisateur(Utilisateur utilisateur) ;
    public List<Location> findByEtat(int etat) ;
    public List<Location> findByEtatOrEtat(int etat,int ouEtat) ;
    public List<Location> findByMaterielAndEtat(Materiel materiel,int etat) ;
    public List<Location> findByEtatNot(int etat) ;


//    public Optional<List<Location>> findByUtilisateur(Utilisateur utilisateur) ;
}



