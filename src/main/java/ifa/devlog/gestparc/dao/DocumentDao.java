package ifa.devlog.gestparc.dao;

import ifa.devlog.gestparc.model.Document;
import ifa.devlog.gestparc.model.Materiel;
import ifa.devlog.gestparc.model.Reparation;
import ifa.devlog.gestparc.model.Retour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DocumentDao extends JpaRepository<Document,Integer> {
    public List<Document> findByMateriel(Materiel materiel) ;
    public List<Document> findByReparation(Reparation reparation) ;
    public List<Document> findByRetour(Retour retour) ;
}
