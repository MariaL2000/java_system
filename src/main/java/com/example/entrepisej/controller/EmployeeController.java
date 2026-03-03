package com.example.entrepisej.controller;

import com.example.entrepisej.dto.ChangePasswordRequest;
import com.example.entrepisej.dto.EmployeeDTO;
import com.example.entrepisej.service.IEmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final IEmployeeService employeeService;

    @GetMapping
    public ResponseEntity<Page<EmployeeDTO>> getAll(Pageable pageable) {
        return ResponseEntity.ok(employeeService.findAll(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeDTO> getById(@PathVariable Long id) {
        return ResponseEntity.ok(employeeService.findById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<EmployeeDTO>> searchByName(@RequestParam String query) {
        return ResponseEntity.ok(employeeService.findByName(query));
    }

    @GetMapping("/salary-range")
    public ResponseEntity<List<EmployeeDTO>> getBySalaryRange(
            @RequestParam Double min,
            @RequestParam Double max) {
        return ResponseEntity.ok(employeeService.findBySalaryRange(min, max));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<EmployeeDTO> createEmployee(
            @RequestPart("employee") String employeeJson, // Lo recibimos como String para parsearlo manualmente si
                                                          // falla el automático
            @RequestPart("image") MultipartFile image) throws IOException {
        // Usamos ObjectMapper para convertir el String JSON a DTO de forma segura
        ObjectMapper objectMapper = new ObjectMapper();
        EmployeeDTO dto = objectMapper.readValue(employeeJson, EmployeeDTO.class);

        return new ResponseEntity<>(employeeService.createWithImage(dto, image), HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<EmployeeDTO> update(
            @PathVariable Long id,
            @RequestPart("employee") @Valid EmployeeDTO employeeDTO,
            @RequestPart(value = "image", required = false) MultipartFile image) throws java.io.IOException {
        // Lógica de actualización similar
        return ResponseEntity.ok(employeeService.updateWithImage(id, employeeDTO, image));
    }

    @DeleteMapping("/{id}")
    // Permite si es Admin O si el usuario que está logueado tiene el mismo ID que
    // quiere borrar
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/change-password")
    @PreAuthorize("#id == authentication.principal.id or hasRole('ADMIN')")
    public ResponseEntity<String> changePassword(
            @PathVariable Long id,
            @Valid @RequestBody ChangePasswordRequest request) {

        employeeService.changePassword(id, request);
        return ResponseEntity.ok("Contraseña actualizada exitosamente");
    }
}