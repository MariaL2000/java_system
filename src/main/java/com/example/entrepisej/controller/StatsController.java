package com.example.entrepisej.controller;

import com.example.entrepisej.repository.TransactionRepository;
import com.example.entrepisej.repository.EmployeeRepository;
import com.example.entrepisej.repository.ContractRepository;
import com.example.entrepisej.dto.ContractDTO; 
import com.example.entrepisej.mapper.Mapper;   
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/stats")
@RequiredArgsConstructor
public class StatsController {

    private final TransactionRepository transactionRepo;
    private final EmployeeRepository employeeRepo;
    private final ContractRepository contractRepo;

    @GetMapping("/total-revenue")
    public ResponseEntity<Double> getTotalRevenue() {
        // Asegúrate de que sumTotalRevenue() exista en TransactionRepository
        Double total = transactionRepo.sumTotalRevenue();
        return ResponseEntity.ok(total != null ? total : 0.0);
    }

    @GetMapping("/employees-count")
    public ResponseEntity<Long> getTotalEmployees() {
        return ResponseEntity.ok(employeeRepo.count());
    }

    @GetMapping("/active-contracts")
    public ResponseEntity<List<ContractDTO>> getActiveContracts() {
        // Cambiamos findByStatus por findByActiveTrue (o el que definas en el Repo)
        return ResponseEntity.ok(
            contractRepo.findByActiveTrue().stream()
                .map(Mapper::toDTO)
                .collect(Collectors.toList())
        );
    }
}