package com.example.hairsalon.controllers.payment;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.hairsalon.responses.TransactionStatusResponse;
import com.example.hairsalon.services.PaymentService;

@RestController
@RequestMapping("/api/customer/payments")
@PreAuthorize("hasAnyRole('USER')")
public class PaymentControllerByCustomer {

    @Autowired
    private PaymentService paymentService;

    // customer
    @GetMapping(value = "/create-payment", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> createPayment_Customer(@RequestParam Integer appointmentID)
            throws UnsupportedEncodingException {
        return paymentService.createPayment(appointmentID);
    }

    // customer
    @GetMapping(value = "/vnpay-return", produces = "application/json;charset=UTF-8")
    public ResponseEntity<?> vnpayReturn(
            @RequestParam(value = "vnp_BankCode") String bankCode,
            @RequestParam(value = "vnp_OrderInfo") Integer appointmentID,
            @RequestParam(value = "vnp_ResponseCode") String responseCode,
            @RequestParam(value = "vnp_TransactionNo", required = false) Integer transactionNo) {
        return paymentService.handlePaymentReturn(bankCode, appointmentID, responseCode, transactionNo);
    }

    @GetMapping("/all-by-account/{accountID}")
    public ResponseEntity<?> getAllPaymentsByAccountID(@PathVariable Long accountID) {
        return paymentService.getPaymentByAccountID(accountID);
    }

    @GetMapping("/appointment/{appointmentID}")
    public ResponseEntity<?> getPaymentByAppointmentID(@PathVariable Integer appointmentID) {
        return paymentService.getPaymentByAppointmentID(appointmentID);
    }

}
