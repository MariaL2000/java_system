package com.example.entrepisej.repository;



import com.example.entrepisej.domain.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.Optional;


public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    Optional<Employee> findByEmail(String email);
    
    
    @Query("SELECT e FROM Employee e WHERE " +
        "LOWER(CONCAT(e.firstName, ' ', e.lastName)) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
        "LOWER(e.email) LIKE LOWER(CONCAT('%', :query, '%'))")
    List<Employee> searchByQuery(@Param("query") String query);


    @Query("SELECT e FROM Employee e JOIN e.contracts c WHERE c.active = true AND c.baseSalary BETWEEN :min AND :max")
    List<Employee> findBySalaryRange(@Param("min") Double min, @Param("max") Double max);
    
    boolean existsByEmail(String email);
    

    // Búsqueda paginada solo de empleados activos
@Query("SELECT e FROM Employee e WHERE e.active = true")
Page<Employee> findAllActive(Pageable pageable);

// Búsqueda por nombre pero solo si están activos
@Query("SELECT e FROM Employee e WHERE e.active = true AND " +
"(LOWER(CONCAT(e.firstName, ' ', e.lastName)) LIKE LOWER(CONCAT('%', :query, '%')))")
List<Employee> searchActiveByQuery(@Param("query") String query);
}