package com.example.hairsalon.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import com.example.hairsalon.models.Services;
import com.example.hairsalon.repositories.ComboDetailRepository;
import com.example.hairsalon.repositories.ComboRepository;
import com.example.hairsalon.repositories.ServiceRepository;
import com.example.hairsalon.requests.ServiceRequest;
import com.google.cloud.storage.Storage;

@Service
@Validated
public class ServicesService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ComboRepository comboRepository;

    @Autowired
    private ComboDetailRepository comboDetailRepository;

    @Autowired
    private Storage storage;

    @Autowired
    private FirebaseStorageService firebaseStorageService;

    // Create service
    @Transactional
    public ResponseEntity<?> createService(ServiceRequest request, MultipartFile file) {

        if (serviceRepository.existsByServiceName(request.getServiceName())) {
            return ResponseEntity.badRequest().body("Service already exists");
        }

        String imageUrl = null;
        if (file != null && !file.isEmpty()) {
            try {
                imageUrl = firebaseStorageService.uploadFile(file, "services/");
            } catch (IOException e) {
                return ResponseEntity.status(500).body("Failed to upload image: " + e.getMessage());
            }
        }

        Services service = Services.builder()
                .serviceName(request.getServiceName())
                .servicePrice(request.getServicePrice())
                .serviceImage(imageUrl)
                .build();

        serviceRepository.save(service);
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
    public ResponseEntity<?> deleteService(Integer id) {
        if (!serviceRepository.existsById(id)) {
            return ResponseEntity.badRequest().body("Service not found");
        }
        comboDetailRepository.deleteByService_ServiceID(id);
        serviceRepository.deleteById(id);
        return ResponseEntity.ok("Service deleted successfully");
    }
}
