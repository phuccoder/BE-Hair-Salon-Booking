package com.example.hairsalon.repositories;


import com.example.hairsalon.models.StylistEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface IStylistRepository extends JpaRepository<StylistEntity, Long> {
    List<StylistEntity> findByStylistStatus(Boolean stylistStatus);

    Optional<StylistEntity> findByStylistEmailOrStylistPhone(String email, String phone);
}