package com.example.entrepisej.auth;

import com.example.entrepisej.auth.request.LoginRequest;
import com.example.entrepisej.auth.request.RegisterRequest;
import com.example.entrepisej.dto.AuthResponse; // IMPORTANTE
import com.example.entrepisej.domain.models.Employee;
import com.example.entrepisej.domain.models.Role;
import com.example.entrepisej.security.JwtService; // IMPORTANTE
import com.example.entrepisej.repository.EmployeeRepository;
import com.example.entrepisej.repository.RoleRepository;

// Excepciones y Utilidades de Java/Spring
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;

// Clases de Java estándar que te faltaban
import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final EmployeeRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        Employee user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        
        String token = jwtService.getToken(user);
        return AuthResponse.builder().token(token).build();
    }

    public AuthResponse register(RegisterRequest request) {
        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new RuntimeException("Error: Rol USER no encontrado"));

        Employee user = Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(userRole))
                .build();

        userRepository.save(user);
        return AuthResponse.builder().token(jwtService.getToken(user)).build();
    }

    @Transactional
    public void deleteAccount(Long id) {
        String currentEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        
        Employee currentUser = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new RuntimeException("Usuario actual no encontrado"));

        Employee userToDelete = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("La cuenta a eliminar no existe"));

                
        boolean isAdmin = currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (currentUser.getId().equals(id) || isAdmin) {
            userRepository.delete(userToDelete);
        } else {
            throw new AccessDeniedException("No tienes permiso para eliminar esta cuenta");
        }
    }
}