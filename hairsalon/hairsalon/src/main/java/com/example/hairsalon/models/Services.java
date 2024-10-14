package com.example.hairsalon.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

import org.springframework.data.relational.core.mapping.Table;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
@Table(name = "services")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Services {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer serviceID;

    @Column(name = "serviceName")
    private String serviceName;

    @Column(name = "servicePrice", precision = 10, scale = 0)
    private BigDecimal servicePrice;

    @ManyToOne
    @JoinColumn(name = "comboID", nullable = true)
    @JsonBackReference
    private Combo combo;
}