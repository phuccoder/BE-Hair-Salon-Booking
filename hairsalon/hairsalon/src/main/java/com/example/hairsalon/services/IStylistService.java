package com.example.hairsalon.services;

import com.example.hairsalon.models.StylistEntity;
import com.example.hairsalon.requests.AccountRequest.AccountSignInRequest;
import com.example.hairsalon.responses.SignInResponse;

import java.util.List;

public interface IStylistService {
    StylistEntity getStylistById(Long id);
    StylistEntity addStylist(StylistEntity stylist);
    List<StylistEntity> getAllStylists();
    List<StylistEntity> getActiveStylists();
    StylistEntity updateStylist(Long id, StylistEntity updatedStylist);
    void deleteStylist(Long id);
}
