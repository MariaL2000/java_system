package com.example.entrepisej.mapper;

import com.example.entrepisej.dto.*;
import com.example.entrepisej.domain.models.*;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

public class Mapper {

    public static EmployeeDTO toDTO(Employee emp) {
        if (emp == null) { 
            throw new RuntimeException("Error interno: Fallo al mapear entidad Employee (nulo)");
        }
        return EmployeeDTO.builder()
                .id(emp.getId())
                .fullName(emp.getFirstName() + " " + emp.getLastName())
                .email(emp.getEmail())
                .imageUrl(emp.getImageUrl())
                .currentSalary(emp.getCurrentSalary())
                .roles(emp.getRoles().stream().map(Role::getName).collect(Collectors.toList()))
                .build();
    }

    // --- NUEVO MÉTODO: De DTO a Entidad ---
    public static Employee toEntity(EmployeeDTO dto) {
        if (dto == null) return null;

        String firstName = "Sin";
        String lastName = "Nombre";

        if (dto.getFullName() != null && !dto.getFullName().trim().isEmpty()) {
            String[] parts = dto.getFullName().trim().split("\\s+", 2);
            firstName = parts[0];
            lastName = (parts.length > 1) ? parts[1] : "";
        }

        return Employee.builder()
                .firstName(firstName)
                .lastName(lastName)
                .email(dto.getEmail())
                .currentSalary(dto.getCurrentSalary())
                // Agregamos contraseña por defecto para evitar el error de base de datos
                .password("default123") 
                .build();
    }

    public static ContractDTO toDTO(Contract c) {
        if (c == null) throw new RuntimeException("Error interno: Fallo al mapear Contract (nulo)");
        return ContractDTO.builder()
                .id(c.getId())
                .position(c.getPosition())
                .baseSalary(c.getBaseSalary())
                .active(c.isActive())
                .employeeId(c.getEmployee().getId())
                .build();
    }

    public static SalaryTransactionDTO toDTO(Transaction t) {
        if (t == null) throw new RuntimeException("Error interno: Fallo al mapear Transaction (nulo)");
        return SalaryTransactionDTO.buildWithCalculations(
            t.getId(), 
            t.getAmount(), 
            t.getPaymentDate().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)
        );
    }

    public static RoleDTO toDTO(Role role) {
        if (role == null) throw new RuntimeException("Error interno: Fallo al mapear Role (nulo)");
        return RoleDTO.builder()
                .id(role.getId())
                .name(role.getName())
                .build();
    }
}