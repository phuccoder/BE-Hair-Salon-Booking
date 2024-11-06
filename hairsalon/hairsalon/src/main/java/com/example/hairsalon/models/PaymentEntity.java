package com.example.hairsalon.models;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "payment")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "paymentID")
    private Integer paymentID;

    @Column(name = "paymentMethod")
    private String paymentMethod;

    @Column(name = "paymentStatus")
    private String paymentStatus;

    @Column(name = "totalPrice", precision = 10, scale = 0)
    private BigDecimal totalPrice;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "paymentDate")
    private String paymentDate;

    @Column(name = "transactionNo")
    private Integer transactionNo;


    @Column(name = "bankCode")
    private String bankCode;

    @Column(name = "responseCode")
    private String responseCode;

    @ManyToOne
    @JoinColumn(name = "accountID", referencedColumnName = "accountID")
    private AccountEntity account;

    @ManyToOne
    @JoinColumn(name = "appointmentID", referencedColumnName = "appointmentID")
    private AppointmentEntity appointment;

}
