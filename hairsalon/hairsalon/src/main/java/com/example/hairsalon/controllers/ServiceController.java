package com.example.hairsalon.controllers;

import com.example.hairsalon.models.Services;
import com.example.hairsalon.requests.ServiceRequest;
import com.example.hairsalon.services.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/services")
public class ServiceController {

    @Autowired
    private ServicesService serviceService;

    // Create service
    @PostMapping
    public ResponseEntity<Services> createService(@RequestBody ServiceRequest request) {
        Services service = serviceService.createService(request);
        return new ResponseEntity<>(service, HttpStatus.CREATED);
    }

    // Get all services
    @GetMapping
    public ResponseEntity<List<Services>> getAllServices() {
        List<Services> services = serviceService.getAllServices();
        return ResponseEntity.ok(services);
    }

    // Get service by ID
    @GetMapping("/{id}")
    public ResponseEntity<Services> getServiceById(@PathVariable Integer id) {
        Optional<Services> service = serviceService.getServiceById(id);
        return service.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    // Update service
    @PutMapping("/{id}")
    public ResponseEntity<Services> updateService(@PathVariable Integer id,
            @RequestBody ServiceRequest request) {
        Services updatedService = serviceService.updateService(id, request);
        return ResponseEntity.ok(updatedService);
    }

    // Delete service
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Integer id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}
