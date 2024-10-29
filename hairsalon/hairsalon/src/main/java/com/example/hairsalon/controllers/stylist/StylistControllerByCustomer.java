package com.example.hairsalon.controllers.stylist;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hairsalon.components.apis.CoreApiResponse;
import com.example.hairsalon.models.StylistEntity;
import com.example.hairsalon.services.IStylistService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/customer/stylist")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('USER')")
public class StylistControllerByCustomer {

    private final IStylistService stylistService;

    @GetMapping("/get-all-stylists")
    public CoreApiResponse<List<StylistEntity>> getAllStylists() {
        return CoreApiResponse.success(stylistService.getAllStylists());
    }

    @GetMapping("/get-by-id/{id}")
    public CoreApiResponse<StylistEntity> getStylistById(@PathVariable Long id) {
        return CoreApiResponse.success(stylistService.getStylistById(id));
    }

    @GetMapping("/active")
    public List<StylistEntity> getActiveStylists() {
        return stylistService.getActiveStylists();
    }
}
