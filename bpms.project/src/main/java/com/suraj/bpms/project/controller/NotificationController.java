package com.suraj.bpms.project.controller;

import com.suraj.bpms.project.dto.notification.NotificationCreateDTO;
import com.suraj.bpms.project.dto.notification.NotificationResponseDTO;
import com.suraj.bpms.project.response.ApiResponse;
import com.suraj.bpms.project.service.NotificationService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/notification")
public class NotificationController {

    private final NotificationService notificationService;


    @GetMapping
    public List<NotificationResponseDTO> getAllNotification(){
        return notificationService.getAllNotification();
    }
    @GetMapping("/{id}")
    public NotificationResponseDTO getNotificationById(@PathVariable Long id){
        return notificationService.getNotificationById(id);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createNotification(@Valid @RequestBody NotificationCreateDTO
                                                                      notificationCreateDTO){
        notificationService.createNotification(notificationCreateDTO);

        return new ResponseEntity<>(new ApiResponse("Notification is created Successfully"), HttpStatus.CREATED);
    }

}
