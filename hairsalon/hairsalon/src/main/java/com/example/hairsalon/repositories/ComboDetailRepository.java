package com.example.hairsalon.repositories;

import com.example.hairsalon.models.Combo;
import com.example.hairsalon.models.ComboDetail;
import com.example.hairsalon.models.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ComboDetailRepository extends JpaRepository<ComboDetail, Integer> {

    void deleteByComboDetailID(Integer comboDetailID);

    void deleteByService_ServiceID(Integer serviceID);

    void deleteByCombo_ComboID(Integer comboID);

    Optional<ComboDetail> findByComboAndService(Combo combo, Services service);
}