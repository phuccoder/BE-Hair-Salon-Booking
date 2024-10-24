package com.example.hairsalon.controllers.appointment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hairsalon.models.StylistEntity;
import com.example.hairsalon.requests.Appointment.AppointmentRequest;
import com.example.hairsalon.services.AppointmentService;
import com.example.hairsalon.services.IStylistService;

@RestController
@RequestMapping("/api/appointment-management")
@PreAuthorize("hasAnyRole('STYLIST', 'ADMIN', 'STAFF')")
public class AppointmentController {

    @Autowired
    private AppointmentService appointmentService;

    @Autowired
    private IStylistService stylistService;


    @PostMapping("/create")
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentRequest request) {
        return appointmentService.createAppointment(request);
    }

    @PostMapping("/create-stylist")
    public ResponseEntity<StylistEntity> addStylist(@RequestBody StylistEntity stylist) {
        StylistEntity createdStylist = stylistService.addStylist(stylist);
        return ResponseEntity.ok(createdStylist);
    }

    @PutMapping("/update-appointment/{appointmentID}")
    public ResponseEntity<?> updateAppointment(@PathVariable Integer appointmentID, @RequestBody AppointmentRequest request) {
        return appointmentService.updateAppointment(appointmentID, request);
    }

    @PutMapping("/cancel-appointment/{appointmentID}")
    public ResponseEntity<?> cancelAppointment(@PathVariable Integer appointmentID) {
        return appointmentService.cancelAppointment(appointmentID);
    }

    @PutMapping("/accept-appointment/{appointmentID}")
    public ResponseEntity<?> acceptAppointment(@PathVariable Integer appointmentID) {
        return appointmentService.confirmAppointment(appointmentID);
    }

}
