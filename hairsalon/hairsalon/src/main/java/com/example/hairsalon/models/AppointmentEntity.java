package com.example.hairsalon.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "appointment")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointmentID")
    private Integer appointmentID;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "appointmentDate", nullable = false)
    private LocalDateTime appointmentDate;

    @Column(name = "appointmentStatus", nullable = false)
    private String appointmentStatus;

    @Column(name = "accountID")
    private Integer accountID;

    @Column(name = "stylistID")
    private Integer stylistID;

    @Column(name = "voucherID")
    private Integer voucherID;

    @Column(name = "appointmentPrice", precision = 10, scale = 0)
    private BigDecimal appointmentPrice;

    @OneToMany(mappedBy = "appointment")
    private List<AppointmentDetailEntity> appointmentDetails;

    @OneToMany(mappedBy = "appointment")
    private List<ReviewEntity> reviews;
}