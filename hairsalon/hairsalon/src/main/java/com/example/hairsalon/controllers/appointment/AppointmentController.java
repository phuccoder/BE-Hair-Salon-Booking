package com.example.hairsalon.controllers.appointment;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.hairsalon.models.AppointmentEntity;
import com.example.hairsalon.models.StylistEntity;
import com.example.hairsalon.requests.Appointment.AppointmentRequest;
import com.example.hairsalon.services.AppointmentService;
import com.example.hairsalon.services.IStylistService;

import java.util.List;

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

    @GetMapping("/gettAll")
    public ResponseEntity<?> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
    }

    @PutMapping("/update-appointment/{appointmentID}")
    public ResponseEntity<?> updateAppointment(@PathVariable Integer appointmentID,
            @RequestBody AppointmentRequest request) {
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

    @GetMapping("/get-by-appointment-id/{appointmentID}")
    public ResponseEntity<?> getAppointmentByID(@PathVariable Integer appointmentID) {
        return appointmentService.getAppointmentByAppointmentID(appointmentID);
    }

}
