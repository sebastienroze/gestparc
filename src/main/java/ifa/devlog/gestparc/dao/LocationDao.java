package ifa.devlog.gestparc.dao;

import ifa.devlog.gestparc.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LocationDao extends JpaRepository<Location,Integer> {
}



