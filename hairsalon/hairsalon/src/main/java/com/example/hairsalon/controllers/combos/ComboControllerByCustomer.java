package com.example.hairsalon.controllers.combos;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hairsalon.services.ComboService;

@RestController
@RequestMapping("/api/customer/combos")
public class ComboControllerByCustomer {

    @Autowired
    private ComboService comboService;

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
}
