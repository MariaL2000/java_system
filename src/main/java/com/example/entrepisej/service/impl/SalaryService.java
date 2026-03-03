package com.example.entrepisej.service.impl;

import com.example.entrepisej.domain.models.Contract;
import com.example.entrepisej.domain.models.Transaction;
import com.example.entrepisej.dto.SalaryTransactionDTO;
import com.example.entrepisej.mapper.Mapper;
import com.example.entrepisej.repository.ContractRepository;
import com.example.entrepisej.repository.TransactionRepository;
import com.example.entrepisej.service.ISalaryService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SalaryService implements ISalaryService {

    private final TransactionRepository transactionRepository;
    private final ContractRepository contractRepository;

    @Override
    @Transactional
    public SalaryTransactionDTO payMonthlySalary(Long employeeId) {
        Contract activeContract = contractRepository.findByEmployeeIdAndActiveTrue(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("No se encontró un contrato activo para el empleado con ID: " + employeeId));

        Transaction newTransaction = Transaction.builder()
                .amount(activeContract.getBaseSalary())
                .paymentDate(LocalDateTime.now())
                .contract(activeContract)
                .build();

        Transaction savedTransaction = transactionRepository.save(newTransaction);
        
        return Mapper.toDTO(savedTransaction);
    }


    @Override
    @Transactional(readOnly = true)
    public List<SalaryTransactionDTO> getEmployeeHistory(Long employeeId) {
        // Buscamos todas las transacciones navegando por el contrato del empleado
        List<Transaction> transactions = transactionRepository.findByContractEmployeeId(employeeId);
        
        // Transformamos a DTO. Si la lista está vacía, devuelve [] (nunca null)
        return transactions.stream()
                .map(Mapper::toDTO)
                .collect(Collectors.toList());
    }
}