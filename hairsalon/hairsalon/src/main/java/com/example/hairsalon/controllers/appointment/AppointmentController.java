package com.example.hairsalon.controllers.appointment;

import com.example.hairsalon.models.AppointmentEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<AppointmentEntity>> getAllAppointments() {
        return ResponseEntity.ok(appointmentService.getAllAppointments());
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
