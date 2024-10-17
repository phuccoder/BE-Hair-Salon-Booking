package com.example.hairsalon.controllers.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.hairsalon.models.Services;
import com.example.hairsalon.requests.ServiceRequest;
import com.example.hairsalon.services.ServicesService;

@RequestMapping("/api/customer/services")
public class ServiceControllerByCustomer {

    @Autowired
    private ServicesService serviceService;

    // Get all services
    @GetMapping("/get-all-services")
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
}
