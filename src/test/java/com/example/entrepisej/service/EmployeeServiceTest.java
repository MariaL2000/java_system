package com.example.entrepisej.service;

import com.example.entrepisej.domain.models.Employee;
import com.example.entrepisej.repository.EmployeeRepository;
import com.example.entrepisej.service.impl.EmployeeService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @InjectMocks
    private EmployeeService employeeService;

    private Employee ricardo;
    private Employee juanAdmin;

    @BeforeEach
    void setUp() {
        // Preparamos los datos de prueba
        ricardo = Employee.builder()
                .id(2L)
                .email("ricardo@empresa.com")
                .active(true)
                .build();

        juanAdmin = Employee.builder()
                .id(3L)
                .email("juan@empresa.com")
                .active(true)
                .build();
    }

    @Test
    void testDeleteLogical_WhenUserIsSelf() {
        // GIVEN: Ricardo está logueado y quiere "borrarse" a sí mismo (ID 2)
        Authentication auth = new UsernamePasswordAuthenticationToken(ricardo, null, 
                List.of(new SimpleGrantedAuthority("USER")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(employeeRepository.findById(2L)).thenReturn(Optional.of(ricardo));

        // WHEN
        employeeService.delete(2L);

        // THEN: Se debe llamar a save() (cambio de estado) y NUNCA a delete()
        verify(employeeRepository).save(argThat(e -> !e.isActive())); 
        verify(employeeRepository, never()).delete(any());
    }

    @Test
    void testDeletePhysical_WhenUserIsAdmin() {
        // GIVEN: Juan Admin está logueado
        Authentication auth = new UsernamePasswordAuthenticationToken(juanAdmin, null, 
                List.of(new SimpleGrantedAuthority("ADMIN")));
        SecurityContextHolder.getContext().setAuthentication(auth);

        when(employeeRepository.findById(2L)).thenReturn(Optional.of(ricardo));

        // WHEN: El Admin borra a Ricardo (ID 2)
        employeeService.delete(2L);

        // THEN: Se debe llamar a delete() físicamente
        verify(employeeRepository).delete(ricardo);
        verify(employeeRepository, never()).save(any());
    }
}