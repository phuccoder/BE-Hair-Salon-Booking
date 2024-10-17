package com.example.hairsalon.requests;

import lombok.*;

@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRefreshRequest {
    private String refreshToken;
}