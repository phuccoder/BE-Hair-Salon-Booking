package com.example.hairsalon.controllers.stylist;

import com.example.hairsalon.components.apis.CoreApiResponse;
import com.example.hairsalon.models.StylistEntity;
import com.example.hairsalon.requests.StylistRequest;
import com.example.hairsalon.services.FirebaseStorageService;
import com.example.hairsalon.services.IStylistService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static com.example.hairsalon.components.mapper.StylistMapper.INSTANCE;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/stylist-management")
@RequiredArgsConstructor
@PreAuthorize("hasAnyRole('ADMIN', 'STAFF')")
public class StylistController {
    private final IStylistService stylistService;

    private final FirebaseStorageService firebaseStorageService;

    @PostMapping(value = "/create-stylist", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createStylist(
            @RequestPart("stylist") String stylistJson,
            @RequestPart("files") List<MultipartFile> files) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            StylistEntity stylistEntity = objectMapper.readValue(stylistJson, StylistEntity.class);

            // Upload các file lên Firebase và lưu trữ URL
            List<String> avatarUrls = new ArrayList<>();
            for (MultipartFile file : files) {
                String avatarUrl = firebaseStorageService.uploadFileStylistAvatar(file, "stylist_avatar/");
                avatarUrls.add(avatarUrl);
            }

            // Bạn có thể quyết định cách lưu trữ nhiều URL, ví dụ như lưu vào một trường trong StylistEntity
            // Hoặc bạn có thể chỉ lưu URL đầu tiên
            if (!avatarUrls.isEmpty()) {
                stylistEntity.setStylistAvatar(avatarUrls.get(0)); // Lưu URL đầu tiên
            }

            // Lưu stylist vào database
            StylistEntity savedStylist = stylistService.addStylist(stylistEntity);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedStylist);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
    }

    @PutMapping(value = "/update/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateStylist(
            @PathVariable Long id,
            @RequestPart("stylist") String stylistJson,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            StylistEntity updatedStylist = objectMapper.readValue(stylistJson, StylistEntity.class);

            // Lấy stylist hiện tại từ database
            StylistEntity existingStylist = stylistService.getStylistById(id);
            if (existingStylist == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Stylist not found");
            }

            // Nếu có file mới, xóa file cũ và upload file mới
            if (file != null && !file.isEmpty()) {
                // Xóa ảnh hiện tại
                firebaseStorageService.deleteFileStylist(existingStylist.getStylistAvatar());

                // Upload ảnh mới
                String newAvatarUrl = firebaseStorageService.uploadFileStylistAvatar(file, "stylist_avatar/");
                existingStylist.setStylistAvatar(newAvatarUrl);
            }

            // Cập nhật thông tin stylist
            existingStylist.setStylistName(updatedStylist.getStylistName());
            existingStylist.setStylistEmail(updatedStylist.getStylistEmail());
            existingStylist.setStylistPhone(updatedStylist.getStylistPhone());
            // Add other fields that need to be updated

            StylistEntity savedStylist = stylistService.updateStylist(id, existingStylist);

            return ResponseEntity.ok(savedStylist);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred: " + e.getMessage());
        }
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

    

    @DeleteMapping("/delete/{id}")
    public CoreApiResponse<Void> deleteStylist(@PathVariable Long id) {
        stylistService.deleteStylist(id);
        return CoreApiResponse.success(null);
    }
}