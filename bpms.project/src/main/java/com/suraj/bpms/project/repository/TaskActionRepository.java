package com.suraj.bpms.project.repository;

import com.suraj.bpms.project.entity.TaskAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskActionRepository extends JpaRepository<TaskAction, Long> {
}
