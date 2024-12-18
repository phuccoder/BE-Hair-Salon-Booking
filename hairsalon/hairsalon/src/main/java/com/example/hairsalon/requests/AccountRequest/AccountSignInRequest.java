package com.example.hairsalon.requests.AccountRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountSignInRequest implements Serializable {
    @NotBlank
    private String emailOrPhone;

    @NotBlank
    private String password;
}
