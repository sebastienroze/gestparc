package ifa.devlog.gestparc.dao;

import ifa.devlog.gestparc.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleDao extends JpaRepository<Role,Integer> {
}
