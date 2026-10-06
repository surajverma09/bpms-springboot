package com.suraj.bpms.project.repository;

import com.suraj.bpms.project.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findByIsDeletedFalse();

    Optional<User> findByIdAndIsDeletedFalse(Long id);
}
