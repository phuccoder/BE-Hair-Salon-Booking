package com.example.hairsalon.requests.AccountRequest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountUpdateRequest {
    private String accountName;

    @Email(message = "Invalid email format")
    private String accountEmail;

    @Pattern(regexp = "^0[0-9]{9}$", message = "Invalid phone number")
    private String accountPhone;

    private String password;

}
