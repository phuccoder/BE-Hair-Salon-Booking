package com.example.hairsalon.services;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.example.hairsalon.config.PaymentConfig;
import com.example.hairsalon.models.AccountEntity;
import com.example.hairsalon.models.AppointmentEntity;
import com.example.hairsalon.models.PaymentEntity;
import com.example.hairsalon.repositories.AppointmentRepository;
import com.example.hairsalon.repositories.IAccountRepository;
import com.example.hairsalon.repositories.PaymentRepository;
import com.example.hairsalon.responses.PaymentDetailResponse;
import com.example.hairsalon.responses.PaymentResponse;
import com.example.hairsalon.responses.TransactionStatusResponse;

@Service
public class PaymentService {

    @Autowired
    private AppointmentRepository appointmentRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private IAccountRepository accountRepository;

    public ResponseEntity<?> createPayment(Integer appointmentID) throws UnsupportedEncodingException {
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String orderType = "VNPAY";
        AppointmentEntity appointment = appointmentRepository.findById(appointmentID).orElse(null);
        if (appointment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Appointment not found");
        }

        // Lấy accountID từ appointment
        Integer accountID = appointment.getAccountID();
        if (accountID == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Account ID not found in appointment");
        }

        // Lấy AccountEntity từ accountID
        AccountEntity account = accountRepository.findById(accountID.longValue()).orElse(null);
        if (account == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Account not found");
        }

        BigDecimal totalAmount = appointment.getAppointmentPrice();
        long amount = totalAmount.longValue() * 100;
        String bankCode = "NCB";
        String vnp_IpAddr = "127.0.0.1";
        String vnp_TmnCode = PaymentConfig.vnp_TmnCode;

        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount));
        vnp_Params.put("vnp_CurrCode", "VND");
        vnp_Params.put("vnp_BankCode", bankCode);
        vnp_Params.put("vnp_TxnRef", String.valueOf(appointment.getAppointmentID()));
        vnp_Params.put("vnp_OrderInfo", "Thanh toan lich hen:" + appointment.getAppointmentID());
        vnp_Params.put("vnp_OrderType", orderType);
        vnp_Params.put("vnp_Locale", "vn");
        vnp_Params.put("vnp_ReturnUrl", PaymentConfig.vnp_ReturnUrl);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.HOUR, 10);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                // Build query
                query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                query.append('=');
                query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String vnp_SecureHash = PaymentConfig.hmacSHA512(PaymentConfig.secretKey, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = PaymentConfig.vnp_PayUrl + "?" + queryUrl;

        // Save payment
        PaymentEntity payment = new PaymentEntity();
        payment.setAppointment(appointment);
        // payment.setAccount(account);
        payment.setPaymentMethod("VNPay");
        payment.setPaymentStatus("Pending");
        payment.setPaymentDate(vnp_CreateDate);
        payment.setBankCode(bankCode);
        payment.setTotalPrice(totalAmount);
        paymentRepository.save(payment);

        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setStatus("Ok");
        paymentResponse.setMessage("Success");
        paymentResponse.setURL(paymentUrl);

        return ResponseEntity.status(HttpStatus.OK).body(paymentResponse);
    }

    public ResponseEntity<TransactionStatusResponse> handlePaymentReturn(String bankCode, Integer appointmentID,
            String responseCode, Integer transactionNo) {
        AppointmentEntity appointment = appointmentRepository.findById(appointmentID).orElse(null);
        if (appointment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new TransactionStatusResponse("No", "Appointment not found", null));
        }

        TransactionStatusResponse transactionStatusResponse = new TransactionStatusResponse();
        if (responseCode.equals("00")) {
            // payment success
            transactionStatusResponse.setStatus("Ok");
            transactionStatusResponse.setMessage("Thanh toán thành công");
            transactionStatusResponse.setData("");

            // Save payment
            PaymentEntity payment = paymentRepository.findByAppointment_AppointmentID(appointmentID);
            payment.setPaymentStatus("Đã thanh toán");
            payment.setBankCode(bankCode);
            payment.setTransactionNo(transactionNo);
            payment.setResponseCode(responseCode);
            paymentRepository.save(payment);

            // Update appointment status
            appointment.setAppointmentStatus("Đã thanh toán");
            appointmentRepository.save(appointment);
        } else {
            // payment fail
            transactionStatusResponse.setStatus("No");
            transactionStatusResponse.setMessage("Thanh toán thất bại");
            transactionStatusResponse.setData("");

            // Save payment
            PaymentEntity payment = paymentRepository.findByAppointment_AppointmentID(appointmentID);
            if (payment == null) {
                transactionStatusResponse.setStatus("No");
                transactionStatusResponse.setMessage("Chưa có thông tin thanh toán");
                transactionStatusResponse.setData("");
                return ResponseEntity.status(HttpStatus.OK).body(transactionStatusResponse);
            } else {
                payment.setPaymentStatus("Thanh toán thất bại");
                payment.setBankCode(bankCode);
                payment.setTransactionNo(transactionNo);
                payment.setResponseCode(responseCode);
                paymentRepository.save(payment);
            }

            // Revert appointment status
            appointment.setAppointmentStatus("Chờ thanh toán");
            appointmentRepository.save(appointment);
        }

        return ResponseEntity.status(HttpStatus.OK).body(transactionStatusResponse);
    }

    private PaymentDetailResponse convertToResponse(PaymentEntity payment) {
        return PaymentDetailResponse.builder()
                .paymentID(payment.getPaymentID())
                .paymentDate(payment.getPaymentDate() != null ? payment.getPaymentDate().toString() : null)
                .accountID(payment.getAccount() != null ? payment.getAccount().getAccountID() : null)
                .paymentMethod(payment.getPaymentMethod())
                .paymentStatus(payment.getPaymentStatus())
                .totalPrice(payment.getTotalPrice())
                .appointmentID(payment.getAppointment() != null ? payment.getAppointment().getAppointmentID() : null)
                .build();
    }

    public ResponseEntity<?> getPaymentByAppointmentID(Integer appointmentID) {
        PaymentEntity payment = paymentRepository.findByAppointment_AppointmentID(appointmentID);
        if (payment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found");
        }
        PaymentDetailResponse response = convertToResponse(payment);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> getPaymentByPaymentID(Integer paymentID) {
        PaymentEntity payment = paymentRepository.findById(paymentID).orElse(null);
        if (payment == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Payment not found");
        }
        PaymentDetailResponse response = convertToResponse(payment);
        return ResponseEntity.ok(response);
    }

    public ResponseEntity<?> getAllPayments() {
        List<PaymentEntity> payments = paymentRepository.findAll();
        List<PaymentDetailResponse> responses = payments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

    public ResponseEntity<?> getPaymentByAccountID(Long accountID) {
        List<PaymentEntity> payments = paymentRepository.findByAccount_AccountID(accountID);
        List<PaymentDetailResponse> responses = payments.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }

}