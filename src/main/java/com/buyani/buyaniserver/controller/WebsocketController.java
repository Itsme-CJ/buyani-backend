package com.buyani.buyaniserver.controller;

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

import com.buyani.buyaniserver.dto.WebsocketDTO;
import com.buyani.buyaniserver.entity.ChatRoom;
import com.buyani.buyaniserver.entity.Message;
import com.buyani.buyaniserver.entity.Transaction;
import com.buyani.buyaniserver.repository.ChatRoomRepo;
import com.buyani.buyaniserver.repository.MessageRepo;

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
    // 1. Echo back to the sender (buyer echo OR seller echo)
    this.simpMessagingTemplate.convertAndSend(topic, message);

    // 2. Always broadcast to the store so the seller receives customer messages
    if (message.getStoreId() != null) {
      String storeTopic = "/topic/store/" + message.getStoreId();
      log.info("Broadcasting to store topic: {}", storeTopic);
      this.simpMessagingTemplate.convertAndSend(storeTopic, message);
    }

    // 3. Always broadcast to the chatroom's linked customer so seller replies reach the buyer
    //    (@ManyToOne is EAGER by default so getUser() is safe without a transaction)
    if (chatroom.getUser() != null) {
      String customerTopic = "/topic/user/" + chatroom.getUser().getUserId();
      if (!customerTopic.equals(topic)) {
        log.info("Broadcasting to customer topic: {}", customerTopic);
        this.simpMessagingTemplate.convertAndSend(customerTopic, message);
      }
    }

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
