package com.example.hairsalon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hairsalon.models.PaymentEntity;

import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<PaymentEntity, Integer> {

    PaymentEntity findByAppointment_AppointmentID(Integer appointmentID);

    List<PaymentEntity> findByAccount_AccountID(Long accountID);

}
