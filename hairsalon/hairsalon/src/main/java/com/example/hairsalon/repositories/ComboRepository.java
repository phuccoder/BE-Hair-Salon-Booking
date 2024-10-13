package com.example.hairsalon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hairsalon.models.ComboEntity;

public interface ComboRepository extends JpaRepository<ComboEntity, Integer>{
    
}
