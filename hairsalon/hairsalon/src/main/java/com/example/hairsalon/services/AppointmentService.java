package com.example.hairsalon.services;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.example.hairsalon.responses.ComboResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.hairsalon.models.AppointmentEntity;
import com.example.hairsalon.models.Schedules;
import com.example.hairsalon.models.AppointmentDetailEntity;
import com.example.hairsalon.models.StylistEntity;
import com.example.hairsalon.repositories.AppointmentDetailRepository;
import com.example.hairsalon.repositories.AppointmentRepository;
import com.example.hairsalon.repositories.SchedulesRepository;
import com.example.hairsalon.repositories.IStylistRepository;
import com.example.hairsalon.repositories.ServiceRepository;
import com.example.hairsalon.requests.Appointment.AppointmentDetailRequest;
import com.example.hairsalon.requests.Appointment.AppointmentRequest;
import com.example.hairsalon.repositories.ComboRepository;

@Service
public class AppointmentService {

    @Autowired
    private IStylistRepository stylistRepository;

    @Autowired
    private SchedulesRepository scheduleRepository;

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private AppointmentDetailRepository appointmentDetailRepository;

    @Autowired
    private ServiceRepository servicesRepository;

    @Autowired
    private ComboRepository comboRepository;

    // Tính tổng tiền từ AppointmentDetail
    public BigDecimal calculateTotalPrice(Integer appointmentID) {
        List<AppointmentDetailEntity> appointmentDetails = appointmentDetailRepository
                .findByAppointment_AppointmentID(appointmentID);
        return appointmentDetails.stream()
                .map(detail -> {
                    if (detail.getService() != null) {
                        return detail.getService().getServicePrice();
                    } else if (detail.getCombo() != null) {
                        return detail.getCombo().getComboPrice();
                    } else {
                        return BigDecimal.ZERO;
                    }
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public ResponseEntity<?> createAppointment(AppointmentRequest request) {
        try {
            StylistEntity stylist = null;

            // Nếu khách chọn stylist
            if (request.getStylistID() != null) {
                Long stylistID = request.getStylistID().longValue();
                stylist = stylistRepository.findById(stylistID).orElse(null);

                if (stylist == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Không tìm thấy stylist với ID đã chọn.");
                }

                // Kiểm tra xem stylist có rảnh không
                if (!isStylistAvailable(stylist.getStylistID(), request.getAppointmentDate())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("Stylist không rảnh trong giờ đã chọn.");
                }
            } else {
                // Khách không chọn stylist, tìm stylist ngẫu nhiên rảnh
                stylist = findAvailableStylist(request.getAppointmentDate());
                if (stylist == null) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("Không có stylist nào rảnh trong giờ đã chọn.");
                }
                request.setStylistID(stylist.getStylistID().intValue());
            }

            // Tạo lịch hẹn
            AppointmentEntity appointment = AppointmentEntity.builder()
                    .appointmentDate(request.getAppointmentDate())
                    .accountID(request.getAccountID())
                    .stylistID(request.getStylistID())
                    .appointmentStatus("CONFIRMED")
                    .build();

            // Lưu lịch hẹn trước khi tính tổng tiền
            AppointmentEntity savedAppointment = appointmentRepository.save(appointment);

            // Lưu chi tiết lịch hẹn
            for (AppointmentDetailRequest detailRequest : request.getDetails()) {
                AppointmentDetailEntity appointmentDetail = AppointmentDetailEntity.builder()
                        .appointment(savedAppointment)
                        .service(detailRequest.getServiceID() != null
                                ? servicesRepository.findById(detailRequest.getServiceID()).orElse(null)
                                : null)
                        .combo(detailRequest.getComboID() != null
                                ? comboRepository.findById(detailRequest.getComboID()).orElse(null)
                                : null)
                        .build();
                appointmentDetailRepository.save(appointmentDetail);
            }

            // Tính tổng tiền sau khi lưu AppointmentDetails
            BigDecimal totalPrice = calculateTotalPrice(savedAppointment.getAppointmentID());
            savedAppointment.setAppointmentPrice(totalPrice);

            // Cập nhật lại lịch hẹn với tổng tiền
            appointmentRepository.save(savedAppointment);

            return ResponseEntity.status(HttpStatus.CREATED).body(savedAppointment);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi tạo lịch hẹn: " + e.getMessage());
        }
    }

    public List<AppointmentEntity> getAllAppointments() {
        return appointmentRepository.findAll();
    }

    // Cập nhật lịch hẹn
    public ResponseEntity<?> updateAppointment(Integer appointmentID, AppointmentRequest request) {
        try {
            Optional<AppointmentEntity> optionalAppointment = appointmentRepository.findById(appointmentID);
            if (!optionalAppointment.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Không tìm thấy lịch hẹn với ID đã chọn.");
            }

            AppointmentEntity appointment = optionalAppointment.get();

            // Nếu khách chọn stylist
            if (request.getStylistID() != null) {
                Long stylistID = request.getStylistID().longValue();
                StylistEntity stylist = stylistRepository.findById(stylistID).orElse(null);

                if (stylist == null) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
                            .body("Không tìm thấy stylist với ID đã chọn.");
                }

                // Kiểm tra xem stylist có rảnh không
                if (!isStylistAvailable(stylist.getStylistID(), request.getAppointmentDate())) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("Stylist không rảnh trong giờ đã chọn.");
                }

                appointment.setStylistID(request.getStylistID());
            } else {
                // Khách không chọn stylist, tìm stylist ngẫu nhiên rảnh
                StylistEntity availableStylist = findAvailableStylist(request.getAppointmentDate());
                if (availableStylist == null) {
                    return ResponseEntity.status(HttpStatus.CONFLICT)
                            .body("Không có stylist nào rảnh trong giờ đã chọn.");
                }
                appointment.setStylistID(availableStylist.getStylistID().intValue());
            }

            // Cập nhật thông tin lịch hẹn
            appointment.setAppointmentDate(request.getAppointmentDate());
            appointment.setAccountID(request.getAccountID());
            appointment.setAppointmentStatus("CONFIRMED");

            // Xóa chi tiết lịch hẹn cũ
            appointmentDetailRepository.deleteByAppointment_AppointmentID(appointmentID);

            // Lưu chi tiết lịch hẹn mới
            for (AppointmentDetailRequest detailRequest : request.getDetails()) {
                AppointmentDetailEntity appointmentDetail = AppointmentDetailEntity.builder()
                        .appointment(appointment)
                        .service(detailRequest.getServiceID() != null
                                ? servicesRepository.findById(detailRequest.getServiceID()).orElse(null)
                                : null)
                        .combo(detailRequest.getComboID() != null
                                ? comboRepository.findById(detailRequest.getComboID()).orElse(null)
                                : null)
                        .build();
                appointmentDetailRepository.save(appointmentDetail);
            }

            // Tính tổng tiền sau khi lưu AppointmentDetails
            BigDecimal totalPrice = calculateTotalPrice(appointmentID);
            appointment.setAppointmentPrice(totalPrice);

            // Cập nhật lại lịch hẹn với tổng tiền
            appointmentRepository.save(appointment);

            return ResponseEntity.status(HttpStatus.OK).body(appointment);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi cập nhật lịch hẹn: " + e.getMessage());
        }
    }

    // Huỷ lịch hẹn
    public ResponseEntity<?> cancelAppointment(Integer appointmentID) {
        try {
            Optional<AppointmentEntity> optionalAppointment = appointmentRepository.findById(appointmentID);
            if (!optionalAppointment.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Không tìm thấy lịch hẹn với ID đã chọn.");
            }
        
            AppointmentEntity appointment = optionalAppointment.get();
            appointment.setAppointmentStatus("CANCELLED");
            appointmentRepository.save(appointment);
        
            return ResponseEntity.status(HttpStatus.OK).body("Lịch hẹn đã được huỷ.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi huỷ lịch hẹn: " + e.getMessage());
        }
    }

    // Xác nhận lịch hẹn
    public ResponseEntity<?> confirmAppointment(Integer appointmentID) {
        try {
            Optional<AppointmentEntity> optionalAppointment = appointmentRepository.findById(appointmentID);
            if (!optionalAppointment.isPresent()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Không tìm thấy lịch hẹn với ID đã chọn.");
            }

            AppointmentEntity appointment = optionalAppointment.get();
            appointment.setAppointmentStatus("SCHEDULED");
            appointmentRepository.save(appointment);

            return ResponseEntity.status(HttpStatus.OK).body("Lịch hẹn đã được xác nhận.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Đã xảy ra lỗi khi xác nhận lịch hẹn: " + e.getMessage());
        }
    }

    // Kiểm tra xem stylist có rảnh không
    private boolean isStylistAvailable(Long stylistID, LocalDateTime appointmentDate) {
        List<AppointmentEntity> existingAppointments = appointmentRepository
                .findByStylistIDAndAppointmentDate(stylistID, appointmentDate);
        if (!existingAppointments.isEmpty()) {
            return false;
        }

        List<Schedules> schedules = scheduleRepository.findByStylist_StylistID(stylistID);
        for (Schedules schedule : schedules) {
            if (schedule.getDayOfWeek().name().equalsIgnoreCase(appointmentDate.getDayOfWeek().name())) {
                LocalTime appointmentTime = appointmentDate.toLocalTime();
                if (!appointmentTime.isBefore(schedule.getStartTime())
                        && !appointmentTime.isAfter(schedule.getEndTime())) {
                    return true;
                }
            }
        }
        return false;
    }

    // Tìm stylist ngẫu nhiên rảnh
    private StylistEntity findAvailableStylist(LocalDateTime appointmentDate) {
        List<StylistEntity> allStylists = stylistRepository.findAll();
        for (StylistEntity stylist : allStylists) {
            if (isStylistAvailable(stylist.getStylistID(), appointmentDate)) {
                return stylist;
            }
        }
        return null;
    }
}