package com.example.hairsalon.services;

import com.example.hairsalon.models.Services;
import com.example.hairsalon.models.ComboEntity;
import com.example.hairsalon.repositories.ServiceRepository;
import com.example.hairsalon.repositories.ComboRepository;
import com.example.hairsalon.requests.ServiceRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import java.util.List;
import java.util.Optional;

@Service
@Validated
public class ServicesService {

    @Autowired
    private ServiceRepository serviceRepository;

    @Autowired
    private ComboRepository comboRepository;

    // Create service
    @Transactional
    public Services createService(ServiceRequest request) {
        Services service = Services.builder()
                .serviceName(request.getServiceName())
                .build();

        if (request.getComboID() != null) {
            ComboEntity combo = comboRepository.findById(request.getComboID())
                    .orElseThrow(() -> new IllegalArgumentException("Combo ID is not found"));
            service.setCombo(combo);
        } else {
            service.setCombo(null);
        }

        return serviceRepository.save(service);
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
    public Services updateService(Integer id, ServiceRequest request) {
        Services existingService = serviceRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Service ID is not found"));

        existingService.setServiceName(request.getServiceName());

        if (request.getComboID() != null) {
            ComboEntity combo = comboRepository.findById(request.getComboID())
                    .orElseThrow(() -> new IllegalArgumentException("Combo ID is not found"));
            existingService.setCombo(combo);
        } else {
            existingService.setCombo(null);
        }

        return serviceRepository.save(existingService);
    }

    // Delete service
    @Transactional
    public void deleteService(Integer id) {
        if (!serviceRepository.existsById(id)) {
            throw new IllegalArgumentException("Service ID is not found");
        }
        serviceRepository.deleteById(id);
    }
}
