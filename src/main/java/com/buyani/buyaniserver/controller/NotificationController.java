// controller/NotificationController.java
package com.buyani.buyaniserver.controller;

import com.buyani.buyaniserver.entity.Notification;
import com.buyani.buyaniserver.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

  private final NotificationService notificationService;

  @GetMapping
  public ResponseEntity<List<Notification>> getNotifications(@RequestParam String email) {
    return ResponseEntity.ok(notificationService.getNotifications(email));
  }

  @PostMapping
  public ResponseEntity<Notification> sendNotification(@RequestBody Map<String, String> body) {
    String email   = body.get("email");
    String title   = body.get("title");
    String message = body.get("message");
    String type    = body.getOrDefault("type", "INFO");
    Notification notif = notificationService.sendNotification(email, title, message, type);
    return ResponseEntity.ok(notif);
  }

  @PutMapping("/read")
  public ResponseEntity<Void> markAllAsRead(@RequestParam String email) {
    notificationService.markAllAsRead(email);
    return ResponseEntity.ok().build();
  }
}