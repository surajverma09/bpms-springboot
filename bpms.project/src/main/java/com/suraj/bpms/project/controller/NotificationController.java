package com.suraj.bpms.project.controller;

import com.suraj.bpms.project.entity.Notification;
import com.suraj.bpms.project.service.NotificationService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;


    @GetMapping
    public List<Notification> getAllNotification(){
        return notificationService.getAllNotification();
    }
    @GetMapping("/{id}")
    public Notification getNotificationById(@PathVariable Long id){
        return notificationService.getNotificationById(id);
    }

    @PostMapping("/{userId}/{processInstanceId}/{taskId}")
    public Notification createNotification(@PathVariable Long userId,
                                           @PathVariable Long processInstanceId,
                                           @PathVariable Long taskId,
                                           @RequestBody Notification notification){
        return notificationService.createNotification(userId, processInstanceId,
                                                    taskId, notification);
    }

}
