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


    @GetMapping()
    public List<NotificationResponseDTO> getAllNotification(@RequestParam Long userId){
        return notificationService.getAllNotification(userId);
    }

    @GetMapping("/{id}")
    public NotificationResponseDTO getNotificationById(@PathVariable Long id, @RequestParam Long userId){
        return notificationService.getNotificationById(id, userId);
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createNotification(@Valid @RequestBody NotificationCreateDTO
                                                                      notificationCreateDTO){
        notificationService.createNotification(notificationCreateDTO);

        return new ResponseEntity<>(new ApiResponse("Notification is created Successfully"), HttpStatus.CREATED);
    }

    @PatchMapping("/{id}/read")
    public ResponseEntity<ApiResponse> updateNotificationRead(@PathVariable Long id,
                                                              @RequestParam Long userId){
        notificationService.updateNotificationRead(id,userId);

        return new ResponseEntity<>(new ApiResponse("Notification marked as read successfully"), HttpStatus.OK);
    }

    @GetMapping("/unread")
    public List<NotificationResponseDTO> getUnreadNotifications(@RequestParam Long userId){

        return notificationService.getUnreadNotifications(userId);
    }

    @GetMapping("/unread/count")
    public Long getCountOfUnreadNotification(@RequestParam Long userId){
        return notificationService.countUnreadNotification(userId);
    }
}
