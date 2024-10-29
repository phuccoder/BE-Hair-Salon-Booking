package com.example.hairsalon.controllers.review;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hairsalon.requests.ReviewRequest;
import com.example.hairsalon.responses.ReviewResponse;
import com.example.hairsalon.services.ReviewService;

@RestController
@RequestMapping("/api/customer/reviews")
@PreAuthorize("hasAnyRole('USER')")
public class ReviewControllerByCustomer {

    @Autowired
    private ReviewService reviewService;

    @PostMapping("/create")
    public ResponseEntity<?> createReview(@RequestBody ReviewRequest request) {
        return reviewService.createReview(request);
    }

    @GetMapping("/get-by-id/{appointmentID}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByAppointmentID(@PathVariable Integer appointmentID) {
        return ResponseEntity.ok(reviewService.getReviewsByAppointmentID(appointmentID));
    }
}