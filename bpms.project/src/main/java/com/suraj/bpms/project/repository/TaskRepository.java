package com.suraj.bpms.project.repository;

import com.suraj.bpms.project.entity.Task;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {

    Long countByAssignedToUserIdAndStatus(Long userId, String status);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT t FROM Task t WHERE t.id = :taskId")
    Optional<Task> findByIdForUpdate(@Param("taskId") Long id);

    List<Task> findAllByAssignedToUserId(Long userId);

    Optional<Task> findByIdAndAssignedToUserId(Long taskId, Long userId);
}