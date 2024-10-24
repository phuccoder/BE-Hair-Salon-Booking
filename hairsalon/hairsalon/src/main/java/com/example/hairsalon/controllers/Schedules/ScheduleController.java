package com.example.hairsalon.controllers.Schedules;

import com.example.hairsalon.requests.Schedules.ScheduleRequest;
import com.example.hairsalon.responses.SchedulesResponse;
import com.example.hairsalon.services.ScheduleService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

import com.example.hairsalon.requests.Schedules.SchedulePutRequest;

@RestController
@RequestMapping("/api/schedules")
@Tag(name = "Schedules", description = "APIs for managing schedules")
@PreAuthorize("hasAnyRole('STYLIST', 'ADMIN', 'STAFF')")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/create")
    @Operation(summary = "Create schedules for a stylist", description = "Create schedules for a stylist with the given details")
    public ResponseEntity<?> createSchedules(@RequestParam Long stylistID, @RequestBody ScheduleRequest request) {
        return scheduleService.createSchedule(stylistID, request);
    }

    @GetMapping("/all")
    @Operation(summary = "Get all schedules", description = "Retrieve all schedules")
    public ResponseEntity<List<SchedulesResponse>> getAllSchedules() {
        return ResponseEntity.ok(scheduleService.getAllSchedules());
    }

    @GetMapping("/stylist/{stylistID}")
    @Operation(summary = "Get schedules by stylist ID", description = "Retrieve schedules for a specific stylist")
    public ResponseEntity<List<SchedulesResponse>> getSchedulesByStylistID(@PathVariable Long stylistID) {
        return ResponseEntity.ok(scheduleService.getSchedulesByStylistID(stylistID));
    }

    @GetMapping("/{scheduleID}")
    @Operation(summary = "Get schedule by ID", description = "Retrieve a specific schedule by its ID")
    public ResponseEntity<SchedulesResponse> getSchedulesById(@PathVariable Integer scheduleID) {
        SchedulesResponse response = scheduleService.getSchedulesById(scheduleID);
        if (response != null) {
            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PutMapping("/update-schedules/{scheduleID}")
    @Operation(summary = "Update schedules", description = "Update schedules with the given details")
    public ResponseEntity<String> updateSchedules(@PathVariable Integer scheduleID, @RequestBody SchedulePutRequest request) {
        return scheduleService.updateSchedule(scheduleID, request);
    }

    @DeleteMapping("/delete/{scheduleID}")
    @Operation(summary = "Delete schedule", description = "Delete a specific schedule by its ID")
    public ResponseEntity<String> deleteSchedule(@PathVariable Integer scheduleID) {
        return scheduleService.deleteSchedule(scheduleID);
    }

    @GetMapping("/get-available-schedules")
    @Operation(summary = "Get available schedules", description = "Retrieve all available schedules")
    public ResponseEntity<List<SchedulesResponse>> getAvailableSchedules() {
        return ResponseEntity.ok(scheduleService.getAvailableSchedules());
    }

}