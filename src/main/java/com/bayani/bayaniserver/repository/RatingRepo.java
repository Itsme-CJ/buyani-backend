package com.bayani.bayaniserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bayani.bayaniserver.entity.Rating;

public interface RatingRepo extends JpaRepository<Rating, Integer> {
}
