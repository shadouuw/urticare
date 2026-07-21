
package com.amine.urticare.resources.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ChatResponse {

    private String reply;

    public ChatResponse() {}

    public ChatResponse(String reply) {
        this.reply = reply;
    }

}