package com.example.hairsalon.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StylistRequest {

    @NotBlank(message = "Stylist name is required")
    private String stylistName;

    @Pattern(regexp = "^0[0-9]{9}$", message = "Invalid phone number")
    private String stylistPhone;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Stylist email is required")
    private String stylistEmail;

    private String stylistInfor;
}
