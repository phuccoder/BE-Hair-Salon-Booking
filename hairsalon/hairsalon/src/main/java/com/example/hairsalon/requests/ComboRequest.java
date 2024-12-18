package com.example.hairsalon.requests;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ComboRequest {
    
    private String comboName;
    private BigDecimal comboPrice;
    private String comboDescription;

    
}
