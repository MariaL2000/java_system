package com.example.entrepisej.service;

import com.example.entrepisej.dto.SalaryTransactionDTO;
import java.util.List;

public interface ISalaryService {
    
    // Procesa el pago del mes actual basado en el contrato activo
    SalaryTransactionDTO payMonthlySalary(Long employeeId);
    
    // Recupera todas las transacciones históricas de un empleado
    List<SalaryTransactionDTO> getEmployeeHistory(Long employeeId);
}