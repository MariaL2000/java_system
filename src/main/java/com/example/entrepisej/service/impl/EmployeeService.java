package com.example.entrepisej.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.Authentication;
import com.example.entrepisej.domain.models.Employee;
import com.example.entrepisej.domain.models.Role;
import com.example.entrepisej.dto.ChangePasswordRequest;
import com.example.entrepisej.dto.EmployeeDTO;
import com.example.entrepisej.exception.ResourceNotFoundException;
import com.example.entrepisej.mapper.Mapper;
import com.example.entrepisej.repository.EmployeeRepository;
import com.example.entrepisej.repository.RoleRepository;
import com.example.entrepisej.service.IEmployeeService;
import com.example.entrepisej.service.ICloudinaryService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EmployeeService implements IEmployeeService {

    private final EmployeeRepository employeeRepository;
    private final RoleRepository roleRepository;
    private final ICloudinaryService cloudinaryService;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional(readOnly = true)
    public Page<EmployeeDTO> findAll(Pageable pageable) {
        // MEJORA: Solo traer empleados activos
        return employeeRepository.findAllActive(pageable)
                .map(Mapper::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public EmployeeDTO findById(Long id) {
        return employeeRepository.findById(id)
                .map(Mapper::toDTO)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado con ID " + id + " no encontrado"));
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> findByName(String query) {

        return employeeRepository.searchByQuery(query)
                .stream()
                .map(Mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeeDTO> findBySalaryRange(Double min, Double max) {
        return employeeRepository.findBySalaryRange(min, max)
                .stream()
                .map(Mapper::toDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public EmployeeDTO create(EmployeeDTO dto) {
        Employee emp = Mapper.toEntity(dto);
        // Asignamos los roles antes de guardar
        assignRoles(emp, dto.getRoles());

        return Mapper.toDTO(employeeRepository.save(emp));
    }

    @Override
    @Transactional
    public EmployeeDTO createWithImage(EmployeeDTO dto, MultipartFile image) throws IOException {
        String imageUrl = null;
        if (image != null && !image.isEmpty()) {
            imageUrl = cloudinaryService.uploadImage(image);
        }

        Employee emp = Mapper.toEntity(dto);
        emp.setImageUrl(imageUrl);
        assignRoles(emp, dto.getRoles());

        return Mapper.toDTO(employeeRepository.save(emp));
    }

    @Override
    @Transactional
    public EmployeeDTO updateWithImage(Long id, EmployeeDTO dto, MultipartFile image) throws IOException {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado con ID " + id + " no encontrado"));

        if (image != null && !image.isEmpty()) {
            employee.setImageUrl(cloudinaryService.uploadImage(image));
        }

        updateEmployeeData(employee, dto);
        return Mapper.toDTO(employeeRepository.save(employee));
    }

    @Override
    @Transactional
    public EmployeeDTO update(Long id, EmployeeDTO dto) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado"));
        updateEmployeeData(employee, dto);
        return Mapper.toDTO(employeeRepository.save(employee));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("No se puede eliminar: Empleado no existe"));

        // Obtener el usuario actual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_admin") || a.getAuthority().equals("ADMIN"));

        if (isAdmin) {
            // EL ADMIN BORRA DE VERDAD
            employee.getRoles().clear(); // Limpiamos relaciones N:M
            employeeRepository.delete(employee);
        } else {
            // EL USUARIO SOLO SE DESACTIVA (Borrado Lógico)
            employee.setActive(false);
            employeeRepository.save(employee);
        }
    }

    // --- MÉTODOS DE APOYO ---

    private void assignRoles(Employee emp, List<String> roleNames) {
        if (roleNames != null && !roleNames.isEmpty()) {
            Set<Role> rolesEntities = new HashSet<>();
            roleNames.forEach(name -> {
                Role role = roleRepository.findByName(name)
                        .orElseThrow(() -> new ResourceNotFoundException("Rol no encontrado: " + name));
                rolesEntities.add(role);
            });
            emp.setRoles(rolesEntities);
        }
    }

    private void updateEmployeeData(Employee employee, EmployeeDTO dto) {
        Employee temp = Mapper.toEntity(dto);
        employee.setFirstName(temp.getFirstName());
        employee.setLastName(temp.getLastName());
        employee.setEmail(dto.getEmail());
        employee.setCurrentSalary(dto.getCurrentSalary());

        // También actualizamos los roles si vienen en el DTO
        if (dto.getRoles() != null) {
            assignRoles(employee, dto.getRoles());
        }
    }

    @Override
    @Transactional
    public void changePassword(Long id, ChangePasswordRequest request) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Empleado no encontrado"));

        // 1. Verificar que la contraseña actual sea correcta
        if (!passwordEncoder.matches(request.getCurrentPassword(), employee.getPassword())) {
            throw new RuntimeException("La contraseña actual es incorrecta");
        }

        // 2. Encriptar y guardar la nueva contraseña
        employee.setPassword(passwordEncoder.encode(request.getNewPassword()));
        employeeRepository.save(employee);
    }
}