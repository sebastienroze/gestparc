package ifa.devlog.gestparc.dao;

import ifa.devlog.gestparc.model.Materiel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MaterielDao extends JpaRepository<Materiel,Integer> {
}
