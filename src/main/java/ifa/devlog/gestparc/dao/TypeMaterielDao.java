package ifa.devlog.gestparc.dao;

import ifa.devlog.gestparc.model.TypeMateriel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TypeMaterielDao extends JpaRepository<TypeMateriel,Integer> {
}
