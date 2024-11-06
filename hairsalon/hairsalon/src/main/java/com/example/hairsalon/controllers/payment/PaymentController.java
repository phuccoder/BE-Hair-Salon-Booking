package com.example.hairsalon.controllers.payment;

import com.example.hairsalon.services.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments-management")
@PreAuthorize("hasAnyRole('STYLIST', 'ADMIN', 'STAFF')")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/appointment/{appointmentID}")
    public ResponseEntity<?> getPaymentByAppointmentID(@PathVariable Integer appointmentID) {
        return paymentService.getPaymentByAppointmentID(appointmentID);
    }

    @GetMapping("/{paymentID}")
    public ResponseEntity<?> getPaymentByPaymentID(@PathVariable Integer paymentID) {
        return paymentService.getPaymentByPaymentID(paymentID);
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @GetMapping("/all-by-account/{accountID}")
    public ResponseEntity<?> getAllPaymentsByAccountID(@PathVariable Long accountID) {
        return paymentService.getPaymentByAccountID(accountID);
    }
}