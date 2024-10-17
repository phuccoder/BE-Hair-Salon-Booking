package com.example.hairsalon.responses;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SignInResponse {
    private String refreshToken;

    private String accessToken;

    private Long accountID;

    private String accountName;

    private String role;
}