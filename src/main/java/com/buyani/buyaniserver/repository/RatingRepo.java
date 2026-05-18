package com.buyani.buyaniserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.buyani.buyaniserver.entity.Rating;

public interface RatingRepo extends JpaRepository<Rating, Integer> {
}
