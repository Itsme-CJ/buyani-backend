package com.buyani.buyaniserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.buyani.buyaniserver.entity.ReservationItem;

public interface ReservationItemRepo extends JpaRepository<ReservationItem, Integer> {
}
