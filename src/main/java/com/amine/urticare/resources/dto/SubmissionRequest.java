
package com.amine.urticare.resources.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SubmissionRequest {

    @NotNull(message = "Pill count is required")
    @Min(value = 0, message = "Pill count must be >= 0")
    private Integer pillCount;

    @NotBlank(message = "Frequency is required")
    private String frequency;

    @NotNull(message = "Itch level is required")
    @Min(0) @Max(10)
    private Integer itchLevel;

    @NotBlank(message = "Severity is required")
    private String severity;

    private List<String> affectedAreas;

    private String notes;
}