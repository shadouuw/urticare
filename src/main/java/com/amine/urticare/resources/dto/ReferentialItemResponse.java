package com.amine.urticare.resources.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// Shared shape for simple {id, name} reference lists (gouvernorats,
// specialites). Hospitals use HospitalResponse instead (adds `type`).
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ReferentialItemResponse {

    private Long id;
    private String name;
}
