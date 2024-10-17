package com.example.hairsalon.services;

import com.example.hairsalon.models.Services;
import com.example.hairsalon.models.Combo;
import com.example.hairsalon.models.ComboDetail;
import com.example.hairsalon.repositories.ServiceRepository;
import com.example.hairsalon.repositories.ComboDetailRepository;
import com.example.hairsalon.repositories.ComboRepository;
import com.example.hairsalon.requests.ServiceRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

import org.hibernate.sql.Update;
import org.springframework.http.ResponseEntity;

@Service
@Validated
public class ServicesService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ComboRepository comboRepository;

    @Autowired
    private ComboDetailRepository comboDetailRepository;

    // Create service
    @Transactional
    public ResponseEntity<?> createService(ServiceRequest request) {
        Services service = Services.builder()
                .serviceName(request.getServiceName())
                .servicePrice(request.getServicePrice())
                .build();

        Services savedService = serviceRepository.save(service);
        return ResponseEntity.ok("Service created successfully");
    }

    // Get all services
    public List<Services> getAllServices() {
        return serviceRepository.findAll();
    }

    // Get service by ID
    public Optional<Services> getServiceById(Integer id) {
        return serviceRepository.findById(id);
    }

    // Update service
    @Transactional
    public ResponseEntity<?> updateService(Integer serviceID, ServiceRequest serviceRequest) {
        Services existingService = serviceRepository.findById(serviceID).orElse(null);
        if (existingService == null) {
            return ResponseEntity.badRequest().body("Service ID is not found");
        }

        existingService.setServiceName(serviceRequest.getServiceName());
        existingService.setServicePrice(serviceRequest.getServicePrice());
        serviceRepository.save(existingService);
        return ResponseEntity.ok("Service updated successfully");
    }

    // Delete serviceThe method builder() is undefined for the type
    @Transactional
    public void deleteService(Integer id) {
        if (!serviceRepository.existsById(id)) {
            throw new IllegalArgumentException("Service ID is not found");
        }
        comboDetailRepository.deleteByService_ServiceID(id);
        serviceRepository.deleteById(id);
    }
}
