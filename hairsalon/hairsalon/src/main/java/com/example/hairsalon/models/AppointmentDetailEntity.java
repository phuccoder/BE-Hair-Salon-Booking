package com.example.hairsalon.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "appointmentdetail")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppointmentDetailEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "appointmentDetailID")
    private Integer appointmentDetailID;

    @ManyToOne
    @JoinColumn(name = "appointmentID", referencedColumnName = "appointmentID")
    private AppointmentEntity appointment;

    @ManyToOne
    @JoinColumn(name = "serviceID", referencedColumnName = "serviceID", nullable = true)
    private Services service;

    @ManyToOne
    @JoinColumn(name = "comboID", referencedColumnName = "comboID", nullable = true)
    private Combo combo;

}
