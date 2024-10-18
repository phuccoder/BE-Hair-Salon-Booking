package com.example.hairsalon.controllers;

import com.example.hairsalon.components.apis.CoreApiResponse;
import com.example.hairsalon.models.StylistEntity;
import com.example.hairsalon.requests.StylistRequest;
import com.example.hairsalon.services.IStylistService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import static com.example.hairsalon.components.mapper.StylistMapper.INSTANCE;


import java.util.List;

@RestController
@RequestMapping("/api/stylist")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('STYLIST', 'ADMIN', 'STAFF')")
public class StylistController {

    private final IStylistService stylistService;

    @PostMapping
    public CoreApiResponse<?> addStylist(@Valid @RequestBody StylistRequest stylistRequest) {
        StylistEntity stylist = INSTANCE.toEntity(stylistRequest);
        return CoreApiResponse.success(stylistService.addStylist(stylist));
    }

    @GetMapping("/getAll")
    public CoreApiResponse<List<StylistEntity>> getAllStylists() {
        return CoreApiResponse.success(stylistService.getAllStylists());
    }

    @GetMapping("/{id}")
    public CoreApiResponse<StylistEntity> getStylistById(@PathVariable Long id) {
        return CoreApiResponse.success(stylistService.getStylistById(id));
    }

    @GetMapping("/active")
    public List<StylistEntity> getActiveStylists() {
        return stylistService.getActiveStylists();
    }

    @PutMapping("/{id}")
    public StylistEntity updateStylist(@PathVariable Long id, @RequestBody StylistEntity updatedStylist) {
        return stylistService.updateStylist(id, updatedStylist);
    }

    @DeleteMapping("/{id}")
    public void deleteStylist(@PathVariable Long id) {
        stylistService.deleteStylist(id);
    }
}
