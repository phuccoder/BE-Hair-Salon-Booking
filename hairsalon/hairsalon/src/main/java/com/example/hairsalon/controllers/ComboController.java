package com.example.hairsalon.controllers;

import com.example.hairsalon.requests.ComboRequest;
import com.example.hairsalon.services.ComboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/combos")
@Validated
public class ComboController {

    @Autowired
    private ComboService comboService;

    // Create combo
    @PostMapping("/create-combo")
    public ResponseEntity<?> createCombo(@RequestBody ComboRequest request) {
        return comboService.createCombo(request);
    }

    // Get all combos
    @GetMapping
    public ResponseEntity<List<?>> getAllCombos() {
        return ResponseEntity.ok(comboService.getAllCombos());
    }

    // Get combo by ID
    @GetMapping("/get-combo-by-id/{id}")
    public ResponseEntity<?> getComboById(@PathVariable Integer id) {
        return comboService.getComboById(id);
    }

    // Update combo
    @PutMapping("/update-combo/{id}")
    public ResponseEntity<?> updateCombo(@PathVariable Integer id, @RequestBody ComboRequest request) {
        return comboService.updateCombo(id, request);
    }

    // Delete combo
    @DeleteMapping("/delete-combo/{id}")
    public ResponseEntity<?> deleteCombo(@PathVariable Integer id) {
        return comboService.deleteCombo(id);
    }

    // Add service to combo
    @PostMapping("/add-service-to-combo/{comboID}/{serviceID}")
    public ResponseEntity<?> addServiceToCombo(@PathVariable Integer comboID, @PathVariable Integer serviceID) {
        return comboService.addServiceToCombo(comboID, serviceID);
    }

    // Remove service from combo
    @DeleteMapping("/remove-service-from-combo/{comboID}/{serviceID}")
    public ResponseEntity<?> removeServiceFromCombo(@PathVariable Integer comboID, @PathVariable Integer serviceID) {
        return comboService.removeServiceFromCombo(comboID, serviceID);
    }

    // Get all services in combo
    @GetMapping("/get-all-services-in-combo/{comboID}")
    public ResponseEntity<?> getAllServicesInCombo(@PathVariable Integer comboID) {
        return comboService.getAllServicesInCombo(comboID);
    }

    // Get all combos with services
    @GetMapping("/get-all-combos-with-services/{serviceID}")
    public ResponseEntity<?> getAllCombosWithServices(@PathVariable Integer serviceID) {
        return comboService.getAllCombosWithService(serviceID);
    }
}