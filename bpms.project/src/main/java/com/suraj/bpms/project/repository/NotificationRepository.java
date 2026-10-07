package com.suraj.bpms.project.repository;

import com.suraj.bpms.project.entity.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
}
