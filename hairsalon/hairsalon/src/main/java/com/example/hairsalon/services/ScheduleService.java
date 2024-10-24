package com.example.hairsalon.services;

import com.example.hairsalon.models.Schedules;
import com.example.hairsalon.models.StylistEntity;
import com.example.hairsalon.repositories.SchedulesRepository;
import com.example.hairsalon.repositories.IStylistRepository;
import com.example.hairsalon.requests.Schedules.ScheduleDetail;
import com.example.hairsalon.requests.Schedules.ScheduleRequest;
import com.example.hairsalon.responses.SchedulesResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import com.example.hairsalon.requests.Schedules.SchedulePutRequest;

@Service
public class ScheduleService {

    @Autowired
    private SchedulesRepository scheduleRepository;

    @Autowired
    private IStylistRepository stylistRepository;

    private SchedulesResponse convertToResponse(Schedules schedule) {
        return SchedulesResponse.builder()
                .scheduleID(schedule.getScheduleID())
                .dayOfWeek(schedule.getDayOfWeek())
                .startTime(schedule.getStartTime())
                .endTime(schedule.getEndTime())
                .scheduleStatus(schedule.getScheduleStatus())
                .stylistID(schedule.getStylist().getStylistID())
                .stylistName(schedule.getStylist().getStylistName())
                .build();
    }

    public ResponseEntity<String> createSchedule(Long stylistID, ScheduleRequest request) {
        try {
            StylistEntity stylist = stylistRepository.findById(stylistID).orElse(null);
            if (stylist == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Không tìm thấy stylist với ID đã chọn.");
            }

            for (ScheduleDetail detail : request.getDetails()) {
                Schedules schedule = Schedules.builder()
                        .dayOfWeek(detail.getDayOfWeek())
                        .startTime(detail.getStartTime())
                        .endTime(detail.getEndTime())
                        .scheduleStatus("AVAILABLE")
                        .stylist(stylist)
                        .build();
                scheduleRepository.save(schedule);
            }

            return ResponseEntity.status(HttpStatus.CREATED).body("Lịch đã được tạo thành công.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi tạo lịch: " + e.getMessage());
        }
    }

    public ResponseEntity<String> updateSchedule(Integer scheduleID, SchedulePutRequest request) {
        try {
            Schedules existingSchedule = scheduleRepository.findById(scheduleID).orElse(null);
            if (existingSchedule == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Không tìm thấy lịch với ID đã chọn.");
            }

            existingSchedule.setDayOfWeek(request.getDetails().get(0).getDayOfWeek());
            existingSchedule.setStartTime(request.getDetails().get(0).getStartTime());
            existingSchedule.setEndTime(request.getDetails().get(0).getEndTime());
            existingSchedule.setScheduleStatus(request.getScheduleStatus());
            scheduleRepository.save(existingSchedule);

            return ResponseEntity.ok("Lịch đã được cập nhật thành công.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi cập nhật lịch: " + e.getMessage());
        }
    }

    public ResponseEntity<String> deleteSchedule(Integer scheduleID) {
        try {
            Schedules existingSchedule = scheduleRepository.findById(scheduleID).orElse(null);
            if (existingSchedule == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Không tìm thấy lịch với ID đã chọn.");
            }

            scheduleRepository.delete(existingSchedule);
            return ResponseEntity.ok("Lịch đã được xóa thành công.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi xóa lịch: " + e.getMessage());
        }
    }


    public List<SchedulesResponse> getAvailableSchedules() {
        return scheduleRepository.findByScheduleStatus("AVAILABLE").stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<SchedulesResponse> getAllSchedules() {
        return scheduleRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public List<SchedulesResponse> getSchedulesByStylistID(Long stylistID) {
        return scheduleRepository.findByStylist_StylistID(stylistID).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    public SchedulesResponse getSchedulesById(Integer scheduleID) {
        return scheduleRepository.findById(scheduleID)
                .map(this::convertToResponse)
                .orElse(null);
    }
}