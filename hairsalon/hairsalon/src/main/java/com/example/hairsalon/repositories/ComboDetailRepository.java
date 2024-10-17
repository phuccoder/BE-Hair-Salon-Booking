package com.example.hairsalon.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.hairsalon.models.ComboDetail;
import org.springframework.stereotype.Repository;

@Repository
public interface  ComboDetailRepository extends JpaRepository<ComboDetail, Integer> {
    
    void deleteByComboDetailID(Integer comboDetailID);

    void deleteByService_ServiceID(Integer serviceID);

    void deleteByCombo_ComboID(Integer comboID);
}
