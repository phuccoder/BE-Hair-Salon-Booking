package com.example.hairsalon.components.mapper;

import com.example.hairsalon.models.StylistEntity;
import com.example.hairsalon.requests.StylistRequest;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface StylistMapper {
    StylistMapper INSTANCE = Mappers.getMapper(StylistMapper.class);

    StylistEntity toEntity(StylistRequest requestDTO);
}