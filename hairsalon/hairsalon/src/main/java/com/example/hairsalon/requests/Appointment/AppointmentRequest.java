package com.example.hairsalon.requests.Appointment;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AppointmentRequest {

    @Future(message = "Ngày đặt lịch phải là thời gian trong tương lai.")
    @NotNull(message = "Ngày đặt lịch không được để trống.")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime appointmentDate;

    private Integer accountID;
    private Integer stylistID;
    private List<AppointmentDetailRequest> details;
}
