package com.example.hairsalon.controllers.stylist;

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
@RequestMapping("/api/stylist-management")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
public class StylistController {
    private final IStylistService stylistService;

    @PostMapping("/create")
    public CoreApiResponse<StylistEntity> createStylist(@Valid @RequestBody StylistRequest request) {
        StylistEntity stylist = INSTANCE.toEntity(request);
        StylistEntity createdStylist = stylistService.addStylist(stylist);
        return CoreApiResponse.success(createdStylist);
    }

    @GetMapping("/all")
    public CoreApiResponse<List<StylistEntity>> getAllStylists() {
        List<StylistEntity> stylists = stylistService.getAllStylists();
        return CoreApiResponse.success(stylists);
    }

    @GetMapping("/{id}")
    public CoreApiResponse<StylistEntity> getStylistById(@PathVariable Long id) {
        StylistEntity stylist = stylistService.getStylistById(id);
        return CoreApiResponse.success(stylist);
    }

    @PutMapping("/update/{id}")
    public CoreApiResponse<StylistEntity> updateStylist(@PathVariable Long id,
            @Valid @RequestBody StylistRequest request) {
        StylistEntity stylist = INSTANCE.toEntity(request);
        StylistEntity updatedStylist = stylistService.updateStylist(id, stylist);
        return CoreApiResponse.success(updatedStylist);
    }

    @DeleteMapping("/delete/{id}")
    public CoreApiResponse<Void> deleteStylist(@PathVariable Long id) {
        stylistService.deleteStylist(id);
        return CoreApiResponse.success(null);
    }
}