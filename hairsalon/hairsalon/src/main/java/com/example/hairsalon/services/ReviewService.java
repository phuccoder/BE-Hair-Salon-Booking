package com.example.hairsalon.services;

import com.example.hairsalon.models.*;
import com.example.hairsalon.repositories.ReviewRepository;
import com.example.hairsalon.requests.ReviewRequest;
import com.example.hairsalon.responses.ReviewResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public ResponseEntity<?> createReview(ReviewRequest request) {
        try {
            ReviewEntity review = ReviewEntity.builder()
                    .comment(request.getComment())
                    .reviewRating(request.getReviewRating())
                    .account(AccountEntity.builder().accountID(request.getAccountID()).build())
                    .appointment(AppointmentEntity.builder().appointmentID(request.getAppointmentID()).build())
                    .reviewDate(LocalDateTime.now())
                    .build();

            ReviewEntity savedReview = reviewRepository.save(review);
            return ResponseEntity.status(HttpStatus.CREATED).body(convertToResponse(savedReview));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi tạo đánh giá: " + e.getMessage());
        }
    }

    public List<ReviewResponse> getReviewsByAppointmentID(Integer appointmentID) {
        List<ReviewEntity> reviews = reviewRepository.findByAppointment_AppointmentID(appointmentID);
        return reviews.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private ReviewResponse convertToResponse(ReviewEntity review) {
        return ReviewResponse.builder()
                .reviewID(review.getReviewID())
                .comment(review.getComment())
                .reviewRating(review.getReviewRating())
                .accountID(review.getAccount().getAccountID())
                .appointmentID(review.getAppointment().getAppointmentID())
                .reviewDate(review.getReviewDate())
                .build();
    }
}