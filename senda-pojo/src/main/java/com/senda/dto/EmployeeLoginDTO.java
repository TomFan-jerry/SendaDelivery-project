package com.senda.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * EmployeeLogin DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeLoginDTO {

    private String username;
    private String password;
}
