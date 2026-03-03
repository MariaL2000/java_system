package com.example.entrepisej.repository;


import com.example.entrepisej.domain.models.Contract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ContractRepository extends JpaRepository<Contract, Long> {

    // Buscar todos los contratos de un empleado específico
    List<Contract> findByEmployeeId(Long employeeId);

    // Buscar el contrato que está actualmente marcado como activo para un empleado
    Optional<Contract> findByEmployeeIdAndActiveTrue(Long employeeId);

    /**
     * IMPORTANTE: Regla de negocio.
     * Desactiva todos los contratos de un empleado. 
     * Se usa antes de crear uno nuevo para asegurar que solo haya uno activo a la vez.
     */
    @Modifying(clearAutomatically = true) // Esto limpia el caché de JPA tras el update
@Query("UPDATE Contract c SET c.active = false WHERE c.employee.id = :employeeId")
void deactivateAllByEmployeeId(@Param("employeeId") Long employeeId);

List<Contract> findByActiveTrue();

    // Filtro adicional: contratos por encima de un monto específico
    List<Contract> findByBaseSalaryGreaterThanEqualAndActiveTrue(Double salary);
}