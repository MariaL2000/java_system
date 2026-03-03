package com.example.entrepisej.config; 

import com.example.entrepisej.domain.models.Employee;
import com.example.entrepisej.domain.models.Role;
import com.example.entrepisej.repository.EmployeeRepository;
import com.example.entrepisej.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class AdminDataLoader implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
public void run(String... args) throws Exception {
    // 1. Ir a tiro fijo: El rol YA existe gracias al data.sql
    Role adminRole = roleRepository.findByName("ADMIN")
            .orElseThrow(() -> new RuntimeException("ERROR CRÍTICO: El data.sql no cargó los roles"));

    // 2. Crear al Admin si no está
    if (!employeeRepository.existsByEmail("juan@empresa.com")) {
        Employee admin = Employee.builder()
                .firstName("Juan")
                .lastName("Admin")
                .email("juan@empresa.com")
                .password(passwordEncoder.encode("password123"))
                .currentSalary(5000.0)
                .active(true)
                .roles(Set.of(adminRole))
                .build();

        if (admin != null) {
            employeeRepository.save(admin);
        }
        System.out.println(" Admin verificado y listo.");
    }
}
}