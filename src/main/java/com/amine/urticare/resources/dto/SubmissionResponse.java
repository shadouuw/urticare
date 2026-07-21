
package com.amine.urticare.resources.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Setter
@Getter
public class SubmissionResponse {

    private Long id;
    private Integer pillCount;
    private String frequency;
    private Integer itchLevel;
    private String severity;
    private List<String> affectedAreas;
    private String notes;
    private List<String> photoUrls;
    private LocalDateTime createdAt;

}
