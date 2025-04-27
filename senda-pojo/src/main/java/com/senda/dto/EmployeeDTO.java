package com.senda.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * Employee DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeDTO {

    private Long id; // id
    private String name; // name
    private String username; // username
    private String phone; // phone
    private String sex; // sex
    private String idNumber; // idNumber
}
