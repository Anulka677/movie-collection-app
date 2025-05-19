package com.example.moviecollection.repository;

import com.example.moviecollection.model.Director;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DirectorRepository extends JpaRepository<Director, Long> {
}

