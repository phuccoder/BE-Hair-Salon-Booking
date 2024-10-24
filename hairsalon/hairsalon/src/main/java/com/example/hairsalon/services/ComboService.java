package com.example.hairsalon.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.example.hairsalon.models.Combo;
import com.example.hairsalon.models.ComboDetail;
import com.example.hairsalon.models.Services;
import com.example.hairsalon.repositories.ComboDetailRepository;
import com.example.hairsalon.repositories.ComboRepository;
import com.example.hairsalon.repositories.ServiceRepository;
import com.example.hairsalon.requests.ComboRequest;
import com.example.hairsalon.responses.ComboDetailResponse;
import com.example.hairsalon.responses.ComboResponse;

@Service
@Validated
public class ComboService {

    @Autowired
    private ComboRepository comboRepository;

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ComboDetailRepository comboDetailRepository;

    // Create combo
    @Transactional
    public ResponseEntity<?> createComboWithServices(ComboRequest request, List<Integer> serviceIds) {
        // Check if combo name already exists
        if (comboRepository.existsByComboName(request.getComboName())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Combo already exists");
        }

        Combo combo = Combo.builder()
                .comboName(request.getComboName())
                .comboPrice(new BigDecimal(request.getComboPrice()))
                .comboDescription(request.getComboDescription())
                .build();

        comboRepository.save(combo);

        // If serviceIds is null or empty, skip adding services
        if (serviceIds != null && !serviceIds.isEmpty()) {
            // Filter out null service IDs
            List<Integer> validServiceIds = serviceIds.stream()
                    .filter(serviceId -> serviceId != null)
                    .collect(Collectors.toList());

            // Tính tổng giá của các dịch vụ được thêm vào combo
            BigDecimal totalServicePrice = BigDecimal.ZERO;
            for (Integer serviceId : validServiceIds) {
                Optional<Services> serviceOptional = serviceRepository.findById(serviceId);
                if (serviceOptional.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body("Service not found with ID: " + serviceId);
                }
                Services service = serviceOptional.get();
                totalServicePrice = totalServicePrice.add(service.getServicePrice());

                ComboDetail comboDetail = ComboDetail.builder()
                        .combo(combo)
                        .service(service)
                        .build();
                comboDetailRepository.save(comboDetail);
            }

            // tổng giá các dịch vụ <= giá của combo
            if (totalServicePrice.compareTo(combo.getComboPrice()) > 0) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Total service price exceeds combo price");
            }
        }

        return ResponseEntity.status(HttpStatus.CREATED).body("Combo created with services successfully");
    }

    // Map Combo entity to ComboResponse
    private ComboResponse mapToComboResponse(Combo combo) {
        List<ComboDetailResponse> comboDetailResponses = combo.getComboDetails().stream()
                .map(this::mapToComboDetailResponse)
                .collect(Collectors.toList());

        return ComboResponse.builder()
                .comboID(combo.getComboID())
                .comboName(combo.getComboName())
                .comboPrice(combo.getComboPrice())
                .comboDescription(combo.getComboDescription())
                .comboDetails(comboDetailResponses)
                .build();
    }

    // Map ComboDetail entity to ComboDetailResponse
    private ComboDetailResponse mapToComboDetailResponse(ComboDetail comboDetail) {
        return ComboDetailResponse.builder()
                .comboDetailID(comboDetail.getComboDetailID())
                .serviceID(comboDetail.getService().getServiceID())
                .serviceName(comboDetail.getService().getServiceName())
                .servicePrice(comboDetail.getService().getServicePrice())
                .build();
    }

    // Get all combos
    public List<ComboResponse> getAllCombos() {
        return comboRepository.findAll().stream()
                .map(this::mapToComboResponse)
                .collect(Collectors.toList());
    }

    // Get combo by ID
    public ResponseEntity<?> getComboById(Integer id) {
        Optional<Combo> comboOptional = comboRepository.findById(id);

        if (comboOptional.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Combo not found");
        }

        Combo combo = comboOptional.get();
        return ResponseEntity.status(HttpStatus.OK).body(mapToComboResponse(combo));
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
        comboDetailRepository.deleteByCombo_ComboID(id);
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

        // Calculate total price of services in combo
        BigDecimal totalServicePrice = combo.getComboDetails().stream()
                .map(cd -> cd.getService().getServicePrice())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalServicePrice = totalServicePrice.add(service.getServicePrice());

        // Check if total service price exceeds combo pric
        if (totalServicePrice.compareTo(combo.getComboPrice()) > 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Total service price exceeds combo price");
        }

        ComboDetail comboDetail = ComboDetail.builder()
                .combo(combo)
                .service(service)
                .build();

        comboDetailRepository.save(comboDetail);

        return ResponseEntity.status(HttpStatus.OK).body("Service added to combo successfully");
    }

}
