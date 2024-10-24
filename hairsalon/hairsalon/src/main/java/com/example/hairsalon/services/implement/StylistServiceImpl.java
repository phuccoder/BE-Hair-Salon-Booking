package com.example.hairsalon.services.implement;

import com.example.hairsalon.models.StylistEntity;
import com.example.hairsalon.repositories.IStylistRepository;
import com.example.hairsalon.services.IStylistService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StylistServiceImpl implements IStylistService {

    @Autowired
    private IStylistRepository stylistRepository;

    @Override
    public StylistEntity getStylistById(Long id) {
        return stylistRepository.findById(id).orElse(null);
    }

    @Override
    public StylistEntity addStylist(StylistEntity stylist) {
        return stylistRepository.save(stylist);
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
        if (stylistRepository.existsById(id)) {
            updatedStylist.setStylistID(id);
            return stylistRepository.save(updatedStylist);
        }
        return null;
    }

    @Override
    public void deleteStylist(Long id) {
        stylistRepository.deleteById(id);
    }
}