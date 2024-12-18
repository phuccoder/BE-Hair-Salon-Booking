package com.example.hairsalon.controllers.combos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hairsalon.requests.ComboRequest;
import com.example.hairsalon.requests.CreateComboRequest;
import com.example.hairsalon.services.ComboService;

@RestController
@RequestMapping("/api/combos-management")
@PreAuthorize("hasAnyRole('STYLIST', 'ADMIN', 'STAFF')")
@Validated
public class ComboController {

    @Autowired
    private ComboService comboService;

    // Create combo
    @PostMapping("/create-combo")
    public ResponseEntity<?> createCombo(@RequestBody CreateComboRequest createComboRequest) {
        return comboService.createComboWithServices(createComboRequest.getComboRequest(),
                createComboRequest.getServiceIds());
    }

    // Get all combos
    @GetMapping("/get-all-combos")
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

    // Delete service from combo
    @DeleteMapping("/remove-service-from-combo/{comboID}/{serviceID}")
    public ResponseEntity<?> deleteServiceFromCombo(@PathVariable Integer comboID, @PathVariable Integer serviceID) {
        return comboService.removeServiceFromCombo(comboID, serviceID);
    }
}