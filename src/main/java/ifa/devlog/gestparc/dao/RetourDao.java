package ifa.devlog.gestparc.dao;

import ifa.devlog.gestparc.model.Retour;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RetourDao extends JpaRepository<Retour,Integer> {
}
