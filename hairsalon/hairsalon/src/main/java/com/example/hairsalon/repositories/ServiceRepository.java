package com.example.hairsalon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.hairsalon.models.Services;

public interface ServiceRepository extends JpaRepository<Services, Integer> {
}