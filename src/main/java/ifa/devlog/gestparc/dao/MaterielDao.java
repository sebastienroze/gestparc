package ifa.devlog.gestparc.dao;

import ifa.devlog.gestparc.model.Materiel;
import ifa.devlog.gestparc.model.TypeMateriel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MaterielDao extends JpaRepository<Materiel,Integer> {
    public List<Materiel> findByTypeMateriel(TypeMateriel typeMateriel) ;
}
