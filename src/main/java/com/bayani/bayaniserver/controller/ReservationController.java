package com.bayani.bayaniserver.controller;

import java.io.IOException;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bayani.bayaniserver.dto.ReservationDTO;
import com.bayani.bayaniserver.entity.Reservation;
import com.bayani.bayaniserver.service.ReservationService;

@RestController
@RequestMapping("/api/reservation")
public class ReservationController {

  @Autowired
  ReservationService reservationService;

  @PatchMapping("/{id}")
  public ResponseEntity<Object> voidTransaction(@PathVariable Integer id, @RequestBody ReservationDTO request) throws MessagingException, IOException {
    Reservation response = reservationService.updateReservation(request, id);
    if (response != null) {
      return new ResponseEntity<>(response, HttpStatus.OK);
    }
    return new ResponseEntity<>("BadRequest", HttpStatus.BAD_REQUEST);
  }
}
