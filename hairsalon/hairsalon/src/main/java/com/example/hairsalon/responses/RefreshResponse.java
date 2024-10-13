package com.example.hairsalon.responses;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RefreshResponse {
    private String accessToken;
}