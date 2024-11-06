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
public class PaymentDetailResponse {

    private Integer paymentID;
    private String paymentDate;
    private Long accountID;
    private String paymentMethod;
    private String paymentStatus;
    private BigDecimal totalPrice;
    private Integer appointmentID;

}
