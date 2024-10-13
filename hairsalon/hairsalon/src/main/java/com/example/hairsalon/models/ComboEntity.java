package com.example.hairsalon.models;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.relational.core.mapping.Table;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "combo")
@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ComboEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comboID")
    private Integer comboID;

    @Column(name = "comboName")
    private String comboName;

    @Column(name = "comboPrice")
    private BigDecimal comboPrice;

    @Column(name = "comboDescription")
    private String comboDescription;

    @OneToMany(mappedBy = "combo")
    private List<Services> services;
}
