package ifa.devlog.gestparc.dao;

import ifa.devlog.gestparc.model.Historique;
import ifa.devlog.gestparc.model.Materiel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoriqueDao extends JpaRepository<Historique,Integer> {
    public List<Historique> findByMateriel(Materiel materiel) ;
}
