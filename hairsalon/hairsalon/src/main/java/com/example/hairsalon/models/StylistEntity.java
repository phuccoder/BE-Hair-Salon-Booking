package com.example.hairsalon.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Entity
@Table(name = "stylist")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StylistEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "stylistID")
    private Long stylistID;

    @Column(name = "stylistName", nullable = false)
    private String stylistName;

    @Pattern(regexp = "^0[0-9]{9}$", message = "Invalid phone number")
    @Column(name = "stylistPhone", nullable = false)
    private String stylistPhone;

    @Email
    @Column(name = "stylistEmail", nullable = false, unique = true)
    private String stylistEmail;

    @Column(name = "stylistPassword", nullable = false)
    private String stylistPassword = "123Stylist"; // Default password

    @Column(name = "stylistStatus", nullable = false)
    private Boolean stylistStatus = true;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "stylistInfor")
    private String stylistInfor;
}