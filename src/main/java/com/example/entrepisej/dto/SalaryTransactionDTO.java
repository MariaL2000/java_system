package com.example.entrepisej.dto;


import lombok.Builder;
import lombok.Data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class SalaryTransactionDTO {
    private Long id;
    private Double grossAmount;
    private Double netAmount; // Calculado
    private Double taxWithheld; // Calculado
    private String paymentDate;

    // Lógica de cálculo en el DTO
    public static SalaryTransactionDTO buildWithCalculations(Long id, Double gross, String date) {
        double tax = gross * 0.12; // 12% impuesto fijo
        return SalaryTransactionDTO.builder()
                .id(id)
                .grossAmount(gross)
                .taxWithheld(tax)
                .netAmount(gross - tax)
                .paymentDate(date)
                .build();
    }
}