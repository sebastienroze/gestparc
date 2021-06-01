package ifa.devlog.gestparc.dao;

import ifa.devlog.gestparc.model.Cadre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CadreDao extends JpaRepository<Cadre,Integer> {
}
