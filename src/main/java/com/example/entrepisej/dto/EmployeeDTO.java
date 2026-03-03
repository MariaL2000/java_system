package com.example.entrepisej.dto;


import lombok.Builder;
import lombok.Data;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private Long id;
    private String fullName;
    private String email;
    private List<String> roles;
    private Double currentSalary;
    private String imageUrl;
}