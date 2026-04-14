package com.bayani.bayaniserver.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bayani.bayaniserver.dto.ChatRoomDTO;
import com.bayani.bayaniserver.entity.ChatRoom;
import com.bayani.bayaniserver.service.ChatRoomService;

@RestController
@RequestMapping("/api/chatRoom")
public class ChatRoomController {
  
  @Autowired
  ChatRoomService chatRoomService;

  @PostMapping("/create")
  public ResponseEntity<Object> createChatRoom(@RequestBody ChatRoomDTO request) {
    ChatRoom chatRoom = chatRoomService.createChatRoom(request);
    if (chatRoom != null) {
      return new ResponseEntity<>(chatRoom, HttpStatus.OK);
    }
    return new ResponseEntity<>("BadRequest", HttpStatus.BAD_REQUEST);
  }

  @PostMapping("/findByUserId")
  public ResponseEntity<Object> getChatRoom(@RequestBody ChatRoomDTO request) {
    ChatRoom chatRoom = chatRoomService.getChatRoom(request);
    if (chatRoom != null) {
      return new ResponseEntity<>(chatRoom, HttpStatus.OK);
    }
    return new ResponseEntity<>("BadRequest", HttpStatus.BAD_REQUEST);
  }
}
