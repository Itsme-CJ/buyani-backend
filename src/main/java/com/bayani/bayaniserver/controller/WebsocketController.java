package com.bayani.bayaniserver.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import com.bayani.bayaniserver.dto.WebsocketDTO;
import com.bayani.bayaniserver.entity.ChatRoom;
import com.bayani.bayaniserver.entity.Message;
import com.bayani.bayaniserver.entity.Transaction;
import com.bayani.bayaniserver.repository.ChatRoomRepo;
import com.bayani.bayaniserver.repository.MessageRepo;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("api/websocket")
public class WebsocketController {
  @Autowired
  private SimpMessagingTemplate simpMessagingTemplate;

  @Autowired
  MessageRepo messageRepo;

  @Autowired
  ChatRoomRepo chatRoomRepo;

  // Store to System Admin
  public ResponseEntity<WebsocketDTO> sendNotification(WebsocketDTO message) {
    String topic = "/topic/notif/" + message.getStoreId();
    log.info("Topic: {}", topic);
    log.info("Message Received: {}", message);

    this.simpMessagingTemplate.convertAndSend(topic, message);

    return ResponseEntity.ok(message);
  }

  public ResponseEntity<WebsocketDTO> getMessage(WebsocketDTO message) {
    String topic = "/topic/store/" + message.getStoreId();
    log.info("Topic: {}", topic);
    log.info("Message Received: {}", message);

    Message newMessage = new Message();
    newMessage.setContent(message.getContent());
    newMessage.setTo(message.getTo());
    
    Optional<ChatRoom> chOptional = chatRoomRepo.findByChatRoomId(message.getChatRoomId());
    if (chOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    ChatRoom chatroom = chOptional.get();
    newMessage.setChatRoom(chatroom);
    messageRepo.save(newMessage);

    this.simpMessagingTemplate.convertAndSend(topic, message);

    return ResponseEntity.ok(message);
  }


  public ResponseEntity<WebsocketDTO> getMessage2(WebsocketDTO message) {
    String topic = "/topic/user/" + message.getUserId();
    log.info("Topic: {}", topic);
    log.info("Message Received: {}", message);

    Message newMessage = new Message();
    newMessage.setContent(message.getContent());
    newMessage.setTo(message.getTo());
    
    Optional<ChatRoom> chOptional = chatRoomRepo.findByChatRoomId(message.getChatRoomId());
    if (chOptional.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    ChatRoom chatroom = chOptional.get();
    newMessage.setChatRoom(chatroom);
    messageRepo.save(newMessage);
    this.simpMessagingTemplate.convertAndSend(topic, message);

    return ResponseEntity.ok(message);
  }

  @PostMapping("/system-admin")
  public ResponseEntity<WebsocketDTO> sendMessageToSysAd(@RequestBody WebsocketDTO payload) {
    return getMessage(payload);
  }

  @PostMapping("/store")
  public ResponseEntity<WebsocketDTO> sendMessageToStore(@RequestBody WebsocketDTO payload) {
    return getMessage2(payload);
  }
}
