package com.example.hairsalon.controllers.services;

import com.example.hairsalon.models.Services;
import com.example.hairsalon.requests.ServiceRequest;
import com.example.hairsalon.services.ServicesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/services-management")
public class ServiceController {

    @Autowired
    private ServicesService serviceService;

    // Create service
    @PostMapping
    public ResponseEntity<?> createService(@RequestBody ServiceRequest request) {
        return serviceService.createService(request);
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
    public ResponseEntity<Void> deleteService(@PathVariable Integer id) {
        serviceService.deleteService(id);
        return ResponseEntity.noContent().build();
    }
}
