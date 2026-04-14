package com.bayani.bayaniserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bayani.bayaniserver.entity.ReservationItem;

public interface ReservationItemRepo extends JpaRepository<ReservationItem, Integer> {
}
