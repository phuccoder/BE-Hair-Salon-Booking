package com.example.hairsalon.requests;

import com.example.hairsalon.components.validations.FieldMatch;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldMatch(first = "confirmPassword", second = "password", message = "Mật khẩu lần 2 không trung khớp")
public class AccountSignUpRequest {
    @NotEmpty(message = "Full Name is required")
    private String accountName;

    @NotEmpty(message = "Email is required")
    @Email(message = "Invalid email format")
    private String accountEmail;

    @NotEmpty(message = "Phone is required")
    @Size(min = 10, max = 10, message = "Phone must have 10 number")
    private String accountPhone;

    @NotEmpty(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    @NotEmpty(message = "Confirm Password is required")
    private String confirmPassword;
}
