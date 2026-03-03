package com.example.entrepisej.service.impl;

import com.example.entrepisej.dto.ContractDTO;
import com.example.entrepisej.mapper.Mapper;
import com.example.entrepisej.domain.models.Contract;
import com.example.entrepisej.domain.models.Employee;
import com.example.entrepisej.repository.ContractRepository;
import com.example.entrepisej.repository.EmployeeRepository;
import com.example.entrepisej.service.IContractService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Objects; // Para validación de nulos

@Service
@RequiredArgsConstructor
public class ContractService implements IContractService {

    private final ContractRepository contractRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    @Transactional
    public ContractDTO createNewContract(ContractDTO dto) {
        // 1. Validación de seguridad para el ID (Quita el warning de Long)
        Long employeeId = Objects.requireNonNull(dto.getEmployeeId(), "El ID del empleado no puede ser nulo");

        // 2. Validar empleado
        Employee emp = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Empleado no encontrado con ID: " + employeeId));

        // 3. Desactivar contratos previos
        deactivateOldContracts(emp.getId());

        // 4. Crear nuevo contrato usando el Builder
        Contract newContract = Contract.builder()
                .position(dto.getPosition())
                .baseSalary(dto.getBaseSalary())
                .startDate(dto.getStartDate())
                .active(true)
                .employee(emp)
                .build();

        // 5. Guardar y mapear (Quita el warning de Contract)
        Contract savedContract = contractRepository.save(newContract);
        return Mapper.toDTO(savedContract);
    }

    @Override
    @Transactional
    public void deactivateOldContracts(Long employeeId) {
        if (employeeId != null) {
            contractRepository.deactivateAllByEmployeeId(employeeId);
        }
    }
}