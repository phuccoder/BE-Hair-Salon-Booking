package com.example.hairsalon.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewRequest {

    private String comment;
    private Integer reviewRating;
    private Long accountID;
    private Integer appointmentID;
}