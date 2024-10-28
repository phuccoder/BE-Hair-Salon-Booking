package com.example.hairsalon.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentDetailResponse {

    private Integer appointmentDetailID;

    private Integer serviceID;

    private Integer comboID;

    private String serviceName;

    private String comboName;

    private BigDecimal servicePrice;

    private BigDecimal comboPrice;
}