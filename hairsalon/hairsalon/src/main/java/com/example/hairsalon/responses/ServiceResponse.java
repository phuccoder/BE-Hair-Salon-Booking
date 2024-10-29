package com.example.hairsalon.responses;

import com.example.hairsalon.models.Combo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ServiceResponse {
    private Integer serviceID;
    private String serviceName;
    private BigDecimal servicePrice;
    private String serviceImage;
}