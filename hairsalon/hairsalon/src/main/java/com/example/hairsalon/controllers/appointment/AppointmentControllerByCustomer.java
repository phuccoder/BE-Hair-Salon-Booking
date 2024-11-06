package com.example.hairsalon.controllers.appointment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hairsalon.requests.Appointment.AppointmentRequest;
import com.example.hairsalon.services.AppointmentService;
import com.example.hairsalon.services.IStylistService;

@RestController
@RequestMapping("/api/customer/appointment")
@PreAuthorize("hasAnyRole('USER')")
public class AppointmentControllerByCustomer {
    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private IStylistService stylistService;

    @PostMapping("/create")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentRequest request) {
        return appointmentService.createAppointment(request);
    }

    @GetMapping("/get-by-appointment-id/{appointmentID}")
    public ResponseEntity<?> getAppointmentByID(@PathVariable Integer appointmentID) {
        return appointmentService.getAppointmentByAppointmentID(appointmentID);
    }

    @GetMapping("/get-by-account-id/{accountID}")
    public ResponseEntity<?> getAppointmentByAccountID(@PathVariable Long accountID) {
        return appointmentService.getAppointmentByAccountID(accountID);
    }
}
