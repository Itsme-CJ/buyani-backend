package com.buyani.buyaniserver.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.buyani.buyaniserver.dto.ChatRoomDTO;
import com.buyani.buyaniserver.dto.ChatRoomResponseDTO;
import com.buyani.buyaniserver.entity.ChatRoom;
import com.buyani.buyaniserver.entity.Message;
import com.buyani.buyaniserver.repository.ChatRoomRepo;
import com.buyani.buyaniserver.repository.MessageRepo;
import com.buyani.buyaniserver.service.ChatRoomService;

@RestController
@RequestMapping("/api/chatRoom")
public class ChatRoomController {

  @Autowired
  ChatRoomService chatRoomService;

  @Autowired
  ChatRoomRepo chatRoomRepo;

  @Autowired
  MessageRepo messageRepo;

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

  @GetMapping("/findByUserId")
  public ResponseEntity<Object> getChatRoomsByUserId(@RequestParam Integer userId) {
    List<ChatRoom> chatRooms = chatRoomRepo.findByUserIdWithMessages(userId);
    List<ChatRoomResponseDTO> result = chatRooms.stream().map(room -> {
      ChatRoomResponseDTO dto = new ChatRoomResponseDTO();
      dto.setChatRoomId(room.getChatRoomId());
      dto.setName(room.getName());
      dto.setType(room.getType());
      dto.setTs(room.getTs());
      if (room.getUser() != null) dto.setUserId(room.getUser().getUserId());
      if (room.getStore() != null) dto.setStoreId(room.getStore().getStoreId());
      return dto;
    }).collect(java.util.stream.Collectors.toList());
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/findByStoreId")
  public ResponseEntity<Object> getChatRoomsByStoreId(@RequestParam Integer storeId) {
    List<ChatRoom> chatRooms = chatRoomRepo.findByStoreIdWithMessages(storeId);
    List<ChatRoomResponseDTO> result = chatRooms.stream().map(room -> {
      ChatRoomResponseDTO dto = new ChatRoomResponseDTO();
      dto.setChatRoomId(room.getChatRoomId());
      // Show customer name instead of store name for the seller's inbox
      if (room.getUser() != null && room.getUser().getFirstName() != null) {
        String lastName = room.getUser().getLastName() != null ? " " + room.getUser().getLastName() : "";
        dto.setName(room.getUser().getFirstName() + lastName);
      } else {
        dto.setName(room.getName());
      }
      dto.setType(room.getType());
      dto.setTs(room.getTs());
      if (room.getUser() != null) dto.setUserId(room.getUser().getUserId());
      if (room.getStore() != null) dto.setStoreId(room.getStore().getStoreId());
      return dto;
    }).collect(java.util.stream.Collectors.toList());
    return new ResponseEntity<>(result, HttpStatus.OK);
  }

  @GetMapping("/{id}")
public ResponseEntity<Object> getChatRoomById(@PathVariable Integer id) {
    ChatRoom chatRoom = chatRoomService.getChatRoomById(id);
    if (chatRoom != null) {
        ChatRoomResponseDTO dto = new ChatRoomResponseDTO();
        dto.setChatRoomId(chatRoom.getChatRoomId());
        dto.setName(chatRoom.getName());
        dto.setType(chatRoom.getType());
        dto.setTs(chatRoom.getTs());
        if (chatRoom.getUser() != null) dto.setUserId(chatRoom.getUser().getUserId());
        if (chatRoom.getStore() != null) dto.setStoreId(chatRoom.getStore().getStoreId());
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    return new ResponseEntity<>("Not found", HttpStatus.NOT_FOUND);
}
@GetMapping("/{id}/messages")
public ResponseEntity<Object> getMessagesByChatRoomId(@PathVariable Integer id) {
    List<Message> messages = messageRepo.findByChatRoomChatRoomIdOrderByTs(id);
    return new ResponseEntity<>(messages, HttpStatus.OK);
}
  @PostMapping("/findOrCreate")
public ResponseEntity<Object> findOrCreateChatRoom(@RequestBody ChatRoomDTO request) {
    ChatRoom chatRoom = chatRoomService.getChatRoom(request);
    if (chatRoom == null) {
        chatRoom = chatRoomService.createChatRoom(request);
    }
    if (chatRoom != null) {
        return new ResponseEntity<>(chatRoom, HttpStatus.OK);
    }
    return new ResponseEntity<>("BadRequest", HttpStatus.BAD_REQUEST);
}
}