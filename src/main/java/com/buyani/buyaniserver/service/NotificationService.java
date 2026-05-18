// service/NotificationService.java
package com.buyani.buyaniserver.service;

import com.buyani.buyaniserver.entity.Notification;
import com.buyani.buyaniserver.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

  private final NotificationRepository notificationRepo;
  private final SimpMessagingTemplate messagingTemplate;

  // Save and send notification via WebSocket
  public Notification sendNotification(String email, String title, String message, String type) {
    // Save to DB
    Notification notif = new Notification();
    notif.setEmail(email);
    notif.setTitle(title);
    notif.setMessage(message);
    notif.setType(type);
    notificationRepo.save(notif);

    // Push via WebSocket to the user
    messagingTemplate.convertAndSendToUser(
      email,
      "/queue/notifications",
      notif
    );

    return notif;
  }

  // Get all notifications for a user
  public List<Notification> getNotifications(String email) {
    return notificationRepo.findByEmailOrderByCreatedAtDesc(email);
  }

  // Mark all as read
  public void markAllAsRead(String email) {
    List<Notification> notifs = notificationRepo.findByEmailOrderByCreatedAtDesc(email);
    notifs.forEach(n -> n.setIsRead(true));
    notificationRepo.saveAll(notifs);
  }
}