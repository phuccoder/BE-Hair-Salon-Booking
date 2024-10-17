package com.example.hairsalon.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateComboRequest {
    private ComboRequest comboRequest;
    private List<Integer> serviceIds;
}