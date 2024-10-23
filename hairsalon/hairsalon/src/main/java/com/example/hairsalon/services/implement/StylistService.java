package com.example.hairsalon.services.implement;

import com.example.hairsalon.components.exceptions.ApiException;
import com.example.hairsalon.components.exceptions.DataNotFoundException;
import com.example.hairsalon.models.AccountEntity;
import com.example.hairsalon.models.StylistEntity;
import com.example.hairsalon.repositories.IAccountRepository;
import com.example.hairsalon.repositories.IStylistRepository;
import com.example.hairsalon.requests.AccountRequest.AccountSignInRequest;
import com.example.hairsalon.responses.SignInResponse;
import com.example.hairsalon.services.IStylistService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StylistService implements IStylistService {
    private final PasswordEncoder passwordEncoder;
    @Autowired
    private final IStylistRepository stylistRepository;

    @Autowired
    private final IAccountRepository accountRepository;

    @Override
    public StylistEntity addStylist(StylistEntity stylist) {
        // Check if stylist with the same email or phone number already exists
        Optional<StylistEntity> existStylist = stylistRepository.findByStylistEmailOrStylistPhone(stylist.getStylistEmail(), stylist.getStylistPhone());
        Optional<AccountEntity> existUserPhone = accountRepository.findByAccountPhoneOrAccountEmail(stylist.getStylistPhone(), stylist.getStylistEmail());
        if (existStylist.isPresent()) {
            throw new ApiException(HttpStatus.BAD_REQUEST,"Stylist with this email/phone already exists.");
        }

        if(existUserPhone.isPresent()) {
            throw new ApiException(HttpStatus.BAD_REQUEST,"Stylist with this email/phone already exists.");
        }

        // Set default password if none is provided
        stylist.setStylistPassword(passwordEncoder.encode("123Stylist"));
        stylist.setStylistStatus(true);
        stylist.setRole("stylist");

        return stylistRepository.save(stylist);
    }

    @Override
    public StylistEntity getStylistById(Long id) {
        return stylistRepository.findById(id).orElseThrow(() -> new DataNotFoundException("Stylist", "id", id));
    }

    @Override
    public List<StylistEntity> getAllStylists() {
        return stylistRepository.findAll();
    }

    @Override
    public List<StylistEntity> getActiveStylists() {
        return stylistRepository.findByStylistStatus(true);
    }

    @Override
    public StylistEntity updateStylist(Long id, StylistEntity updatedStylist) {
        StylistEntity existingStylist = stylistRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Stylist not found with ID: " + id));
        existingStylist.setStylistName(updatedStylist.getStylistName());
        existingStylist.setStylistPhone(updatedStylist.getStylistPhone());
        existingStylist.setStylistEmail(updatedStylist.getStylistEmail());
        existingStylist.setRole(updatedStylist.getRole());
        existingStylist.setStylistInfor(updatedStylist.getStylistInfor());
        existingStylist.setStylistStatus(updatedStylist.getStylistStatus());
        return stylistRepository.save(existingStylist);
    }

    @Override
    public void deleteStylist(Long id) {
        stylistRepository.deleteById(id);
    }
}