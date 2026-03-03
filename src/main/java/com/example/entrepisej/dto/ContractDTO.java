package com.example.entrepisej.dto;


import lombok.Builder;
import lombok.Data;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ContractDTO {
    private Long id;
    private String position;
    private Double baseSalary;
    private LocalDate startDate;
    private boolean active;
    private Long employeeId;
}