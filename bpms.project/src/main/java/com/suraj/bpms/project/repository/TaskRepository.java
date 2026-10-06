package com.suraj.bpms.project.repository;

import com.suraj.bpms.project.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskRepository extends JpaRepository<Task, Long> {
}
