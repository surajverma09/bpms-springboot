package com.suraj.bpms.project.repository;

import com.suraj.bpms.project.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {

}
