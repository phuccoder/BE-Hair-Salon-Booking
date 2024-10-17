package com.example.hairsalon.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ComboResponse {
    private Integer comboID;
    private String comboName;
    private BigDecimal comboPrice;
    private String comboDescription;
    private List<ComboDetailResponse> comboDetails;
}