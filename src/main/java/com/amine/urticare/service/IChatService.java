
package com.amine.urticare.service;

import com.amine.urticare.resources.dto.ChatRequest;
import com.amine.urticare.resources.dto.ChatResponse;

public interface IChatService {

    ChatResponse sendPublicMessage(ChatRequest request);

    ChatResponse sendAuthenticatedMessage(String userId, ChatRequest request);
}