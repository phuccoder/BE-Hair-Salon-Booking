package com.example.hairsalon.services;

import com.example.hairsalon.models.StylistEntity;
import java.util.List;

public interface IStylistService {
    StylistEntity getStylistById(Long id);
    StylistEntity addStylist(StylistEntity stylist);
    List<StylistEntity> getAllStylists();
    List<StylistEntity> getActiveStylists();
    StylistEntity updateStylist(Long id, StylistEntity updatedStylist);
    void deleteStylist(Long id);
}
