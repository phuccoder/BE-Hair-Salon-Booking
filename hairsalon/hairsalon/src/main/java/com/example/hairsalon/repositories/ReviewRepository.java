package com.example.hairsalon.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hairsalon.models.ReviewEntity;

@Repository
public interface  ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findByAppointment_AppointmentID(Integer appointmentID);
}
