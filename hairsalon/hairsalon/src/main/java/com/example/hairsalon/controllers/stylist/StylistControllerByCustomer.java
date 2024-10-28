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
@RequestMapping("/api/customer/stylist")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('CUSTOMER')")
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
