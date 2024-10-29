package com.example.hairsalon.controllers.services;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.security.access.prepost.PreAuthorize;

import com.example.hairsalon.models.Services;
import com.example.hairsalon.requests.ServiceRequest;
import com.example.hairsalon.services.ServicesService;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/services-management")
@PreAuthorize("hasAnyRole('STYLIST', 'ADMIN', 'STAFF')")
public class ServiceController {

    @Autowired
    private ServicesService serviceService;

    // Create service
    @PostMapping(value = "/create-service", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createService(
            @RequestPart("serviceRequest") String serviceRequestJson,
            @RequestPart("file") MultipartFile file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            ServiceRequest serviceRequest = objectMapper.readValue(serviceRequestJson, ServiceRequest.class);
            return serviceService.createService(serviceRequest, file);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    // Get all services
    @GetMapping
    public ResponseEntity<List<Services>> getAllServices() {
        List<Services> services = serviceService.getAllServices();
        return ResponseEntity.ok(services);
    }

    // Get service by ID
    @GetMapping("/get-service-by-id/{id}")
    public ResponseEntity<Services> getServiceById(@PathVariable Integer id) {
        Optional<Services> service = serviceService.getServiceById(id);
        return service.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Update service
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateService(@PathVariable Integer id,
            @RequestBody ServiceRequest request) {
        return serviceService.updateService(id, request);
    }

    // Delete service
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteService(@PathVariable Integer id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}
