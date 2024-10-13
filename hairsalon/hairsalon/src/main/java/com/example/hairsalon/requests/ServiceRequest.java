package com.example.hairsalon.requests;

import lombok.Getter;
import lombok.Setter;

import org.springframework.data.relational.core.sql.In;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ServiceRequest {

    private String serviceName;

    private Integer comboID;
    
}
