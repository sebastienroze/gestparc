package ifa.devlog.gestparc.dao;

import ifa.devlog.gestparc.model.Materiel;
import ifa.devlog.gestparc.model.Reparation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReparationDao extends JpaRepository<Reparation,Integer> {
    public List<Reparation> findByMaterielAndEtat(Materiel materiel, int etat) ;
    public List<Reparation> findByEtat(int etat) ;
}
