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
    @Column(name = "stylist_id")
    private Long stylistID;

    @Column(name = "stylist_name", nullable = false)
    private String stylistName;

    @Pattern(regexp = "^0[0-9]{9}$", message = "Invalid phone number")
    @Column(name = "stylist_phone", nullable = false)
    private String stylistPhone;

    @Email
    @Column(name = "stylist_email", nullable = false, unique = true)
    private String stylistEmail;

    @Column(name = "stylist_password", nullable = false)
    private String stylistPassword = "123Stylist";  // Default password

    @Column(name = "stylist_status", nullable = false)
    private Boolean stylistStatus = true;

    @Column(name = "role", nullable = false)
    private String role;

    @Column(name = "stylist_infor")
    private String stylistInfor;
}