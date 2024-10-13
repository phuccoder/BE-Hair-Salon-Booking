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
    private Long accountID;

    private String accountName;

    @Column(nullable = false)
    private String accountPhone;

    @Email
    @Column(nullable = false)
    private String accountEmail;

    @JsonIgnore
    private String password;

    private boolean accountStatus=true;

    private String otp;

    private String role;
}
