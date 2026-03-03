package com.example.entrepisej.service;


import com.example.entrepisej.dto.ContractDTO;


public interface IContractService {
    ContractDTO createNewContract(ContractDTO contractDTO);
    void deactivateOldContracts(Long employeeId);
}