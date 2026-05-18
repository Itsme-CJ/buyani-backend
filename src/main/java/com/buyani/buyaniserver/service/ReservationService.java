package com.buyani.buyaniserver.service;

import java.io.IOException;
import java.util.Optional;

import javax.mail.MessagingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.buyani.buyaniserver.dto.ReservationDTO;
import com.buyani.buyaniserver.entity.Reservation;
import com.buyani.buyaniserver.repository.ReservationRepo;

@Service
public class ReservationService {
  @Autowired
  ReservationRepo reservationRepo;

  @Autowired
  OTPService otpService;

  public Reservation updateReservation(ReservationDTO request, Integer id) throws MessagingException, IOException {
    Optional<Reservation> reservationOpt = reservationRepo.findByReservationId(id);

    if (reservationOpt.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }

    Reservation reservation = reservationOpt.get();
    reservation.setNote(request.getNote());
    reservation.setStatus(request.getStatus());
    reservationRepo.save(reservation);

    if (request.getStatus() == 3) {
      otpService.sendNoticationReservationRejected(reservation);
    } else {
      otpService.sendNoticationReservation(reservation);
    }
    return reservation;
  }

}
