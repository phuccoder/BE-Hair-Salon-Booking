package com.example.hairsalon.services;

import com.example.hairsalon.models.Combo;
import com.example.hairsalon.models.Services;
import com.example.hairsalon.repositories.ComboRepository;
import com.example.hairsalon.repositories.ServiceRepository;
import com.example.hairsalon.requests.ComboRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
@Validated
public class ComboService {

    @Autowired
    private ComboRepository comboRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    // Create combo
    @Transactional
    public ResponseEntity<?> createCombo(ComboRequest request) {
        Combo combo = Combo.builder()
                .comboName(request.getComboName())
                .comboPrice(new BigDecimal(request.getComboPrice()))
                .comboDescription(request.getComboDescription())
                .build();

        comboRepository.save(combo);
        return ResponseEntity.status(HttpStatus.CREATED).body("Combo created successfully");

    }

    // Get all combos
    public List<Combo> getAllCombos() {
        return comboRepository.findAll();
    }

    // Get combo by ID
    public  ResponseEntity<Optional<Combo>> getComboById(Integer id) {
        return ResponseEntity.ok(comboRepository.findById(id));
    }

    // Update combo
    @Transactional
    public ResponseEntity<?> updateCombo(Integer id, ComboRequest request) {
        Optional<Combo> existingComboOptional = comboRepository.findById(id);

        if (existingComboOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Combo not found");
        }

        Combo existingCombo = existingComboOptional.get();
        existingCombo.setComboName(request.getComboName());
        existingCombo.setComboPrice(new BigDecimal(request.getComboPrice()));
        existingCombo.setComboDescription(request.getComboDescription());

        comboRepository.save(existingCombo);

        return ResponseEntity.status(HttpStatus.OK).body("Combo updated successfully");
    }

    // Delete combo
    @Transactional
    public ResponseEntity<?> deleteCombo(Integer id) {
        if (!comboRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Combo not found");
        }
        comboRepository.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Combo deleted successfully");
    }

    // Add service to combo
    @Transactional
    public ResponseEntity<?> addServiceToCombo(Integer comboId, Integer serviceId) {
        Optional<Combo> comboOptional = comboRepository.findById(comboId);
        Optional<Services> serviceOptional = serviceRepository.findById(serviceId);

        if (comboOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Combo not found");
        }

        if (serviceOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
        }

        Combo combo = comboOptional.get();
        Services service = serviceOptional.get();

        service.setCombo(combo);
        serviceRepository.save(service);

        return ResponseEntity.status(HttpStatus.OK).body("Service added to combo successfully");
    }

    // Remove service from combo
    @Transactional
    public ResponseEntity<?> removeServiceFromCombo(Integer comboId, Integer serviceId) {
        Optional<Combo> comboOptional = comboRepository.findById(comboId);
        Optional<Services> serviceOptional = serviceRepository.findById(serviceId);

        if (comboOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Combo not found");
        }

        if (serviceOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
        }

        Combo combo = comboOptional.get();
        Services service = serviceOptional.get();

        service.setCombo(null);
        serviceRepository.save(service);

        return ResponseEntity.status(HttpStatus.OK).body("Service removed from combo successfully");
    }

    // Get all services in combo
    public ResponseEntity<?> getAllServicesInCombo(Integer comboId) {
        Optional<Combo> comboOptional = comboRepository.findById(comboId);

        if (comboOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Combo not found");
        }

        Combo combo = comboOptional.get();
        return ResponseEntity.ok(combo.getServices());
    }

    // Get all combos with service
    public ResponseEntity<?> getAllCombosWithService(Integer serviceId) {
        Optional<Services> serviceOptional = serviceRepository.findById(serviceId);

        if (serviceOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Service not found");
        }

        Services service = serviceOptional.get();
        return ResponseEntity.ok(service.getCombo());
    }
}
