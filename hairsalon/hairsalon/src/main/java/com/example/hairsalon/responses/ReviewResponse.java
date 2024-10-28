package com.example.hairsalon.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {

    private Integer reviewID;
    private String comment;
    private Integer reviewRating;
    private Long accountID;
    private Integer appointmentID;
    private LocalDateTime reviewDate;
}