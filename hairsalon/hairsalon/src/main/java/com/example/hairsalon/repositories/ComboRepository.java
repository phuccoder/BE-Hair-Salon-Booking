package com.example.hairsalon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hairsalon.models.Combo;

public interface ComboRepository extends JpaRepository<Combo, Integer> {

    Combo findByComboID(Integer comboID);

    boolean existsByComboName(String comboName);

}
