package com.example.entrepisej.domain.models;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "employees")
@Getter @Setter 
@Builder
@NoArgsConstructor @AllArgsConstructor
public class Employee implements UserDetails { // <--- Implementación de Seguridad

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String firstName;
    private String lastName;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password; // <--- CAMPO OBLIGATORIO PARA SEGURIDAD

    private Double currentSalary;
    private String imageUrl; // <--- CAMPO PARA CLOUDINARY

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "employee_roles",
        joinColumns = @JoinColumn(name = "employee_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    @Builder.Default
    private Set<Role> roles = new HashSet<>();

    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Contract> contracts = new ArrayList<>();

    // --- MÉTODOS DE USERDETAILS PARA SEGURIDAD REAL ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Convertimos tus Roles a SimpleGrantedAuthority con prefijo ROLE_
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email; // Usamos el email como login
    }

    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }

    @Builder.Default
    private boolean active = true;
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}