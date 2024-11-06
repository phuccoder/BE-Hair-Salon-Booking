package com.example.hairsalon.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hairsalon.models.AppointmentEntity;
import com.example.hairsalon.models.ComboDetail;

@Repository
public interface AppointmentRepository  extends JpaRepository<AppointmentEntity, Integer> {
    List<AppointmentEntity> findByStylistIDAndAppointmentDate(Long stylistID, LocalDateTime appointmentDate);

    List<AppointmentEntity> findByAppointmentID(Integer appointmentID);

    List<AppointmentEntity> findByAccountID(Long accountID);
}
