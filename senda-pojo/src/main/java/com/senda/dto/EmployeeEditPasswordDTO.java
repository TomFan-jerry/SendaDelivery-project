package com.senda.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * EmployeeEditPassword DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor@Builder
public class EmployeeEditPasswordDTO {

    private String newPassword; //newPassword
    private String oldPassword; //oldPassword
}
