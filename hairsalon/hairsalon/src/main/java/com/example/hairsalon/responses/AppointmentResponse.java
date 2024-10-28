package com.example.hairsalon.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AppointmentResponse {

    private Integer appointmentID;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime appointmentDate;

    private String appointmentStatus;

    private Integer accountID;

    private Integer stylistID;

    private Integer voucherID;

    private BigDecimal appointmentPrice;

    private List<AppointmentDetailResponse> appointmentDetails;

    private List<ReviewResponse> reviews;
}