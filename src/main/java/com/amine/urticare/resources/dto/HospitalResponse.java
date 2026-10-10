package com.amine.urticare.resources.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class HospitalResponse {

    private Long id;
    private String name;
    private String type;
}
