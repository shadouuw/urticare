
package com.amine.urticare.resources.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatRequest {

    @NotBlank(message = "Message is required")
    private String message;

    @NotBlank(message = "Language is required")
    private String language;

}