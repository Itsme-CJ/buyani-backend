package com.bayani.bayaniserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bayani.bayaniserver.entity.Message;

public interface MessageRepo extends JpaRepository<Message, Integer> {
}
