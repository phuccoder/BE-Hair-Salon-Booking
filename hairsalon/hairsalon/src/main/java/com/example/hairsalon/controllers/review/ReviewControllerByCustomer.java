package com.example.hairsalon.controllers.review;

import com.example.hairsalon.requests.ReviewRequest;
import com.example.hairsalon.responses.ReviewResponse;
import com.example.hairsalon.services.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer/reviews")
@PreAuthorize("hasAnyRole('CUSTOMER')")
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