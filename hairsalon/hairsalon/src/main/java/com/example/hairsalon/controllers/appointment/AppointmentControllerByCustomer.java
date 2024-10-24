package com.example.hairsalon.controllers.appointment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;

import com.example.hairsalon.requests.Appointment.AppointmentRequest;
import com.example.hairsalon.services.AppointmentService;
import com.example.hairsalon.services.IStylistService;

@RestController
@RequestMapping("/api/customer/appointment")
@PreAuthorize("hasAnyRole('CUSTOMER')")
public class AppointmentControllerByCustomer {
    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private IStylistService stylistService;

    @PostMapping("/create")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentRequest request) {
        return appointmentService.createAppointment(request);
    }
}
