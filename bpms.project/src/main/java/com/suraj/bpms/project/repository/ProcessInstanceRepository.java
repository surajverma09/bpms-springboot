package com.suraj.bpms.project.repository;

import com.suraj.bpms.project.entity.ProcessInstance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProcessInstanceRepository extends JpaRepository<ProcessInstance, Long> {

    @Query("""
    SELECT pi FROM ProcessInstance pi
    WHERE pi.id = :processInstanceId
      AND (
          pi.startedBy.id = :userId OR pi.id IN (
              SELECT t.processInstance.id
              FROM Task t
              WHERE t.assignedToUser.id = :userId
          )
      )
    """)
    Optional<ProcessInstance> findVisibleToUser(
            @Param("processInstanceId") Long processInstanceId,
            @Param("userId") Long userId
    );

    @Query("""
    SELECT DISTINCT pi FROM ProcessInstance pi
    WHERE pi.startedBy.id = :userId OR pi.id IN (
           SELECT t.processInstance.id
           FROM Task t
           WHERE t.assignedToUser.id = :userId
       )
    """)
    List<ProcessInstance> findAllVisibleToUser(@Param("userId") Long userId);
}