package com.example.hairsalon.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Entity
@Table(name = "account")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "accountID")
    private Long accountID;

    @Column(name = "accountName", nullable = false)
    private String accountName;

    @Column(name = "accountPhone", nullable = false)
    private String accountPhone;

    @Email
    @Column(name = "accountEmail", nullable = false, unique = true)
    private String accountEmail;

    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "accountStatus")
    private boolean accountStatus=true;

    @Column(name = "otp")
    private String otp;

    @Column(name = "role")
    private String role;
}
