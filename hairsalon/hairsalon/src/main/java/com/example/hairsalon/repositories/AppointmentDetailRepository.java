package com.example.hairsalon.repositories;

import com.example.hairsalon.models.AppointmentDetailEntity;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface  AppointmentDetailRepository extends JpaRepository<AppointmentDetailEntity, Integer> {

    List<AppointmentDetailEntity> findByAppointment_AppointmentID(Integer appointmentID);

    void deleteByAppointment_AppointmentID(Integer appointmentID);
    
}
