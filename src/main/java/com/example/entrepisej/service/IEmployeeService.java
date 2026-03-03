package com.example.entrepisej.service;

import com.example.entrepisej.dto.ChangePasswordRequest;
import com.example.entrepisej.dto.EmployeeDTO;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IEmployeeService {
    Page<EmployeeDTO> findAll(Pageable pageable);
    EmployeeDTO findById(Long id);
    List<EmployeeDTO> findByName(String query);
    List<EmployeeDTO> findBySalaryRange(Double min, Double max);
    EmployeeDTO create(EmployeeDTO employeeDTO);
    EmployeeDTO update(Long id, EmployeeDTO employeeDTO);
    void delete(Long id);

    EmployeeDTO updateWithImage(Long id, EmployeeDTO dto, MultipartFile image) throws IOException;
    
    EmployeeDTO createWithImage(EmployeeDTO dto, MultipartFile image) throws IOException;
    void changePassword(Long id, ChangePasswordRequest request);
}