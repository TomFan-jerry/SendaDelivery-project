package com.senda.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * CategoryStatus DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryStatusDTO {

    private Long id; // id
    private Integer status; // status
}
