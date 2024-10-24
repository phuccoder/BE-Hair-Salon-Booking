package com.example.hairsalon.repositories;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.hairsalon.models.Schedules;
import com.example.hairsalon.models.StylistEntity;

@Repository
public interface SchedulesRepository extends JpaRepository<Schedules, Integer> {
    List<Schedules> findByStylist_StylistID(Long stylistID);

    List<Schedules> findByScheduleStatus(String status);

}
