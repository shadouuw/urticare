
package com.amine.urticare.resources;

import com.amine.urticare.resources.dto.ChatRequest;
import com.amine.urticare.resources.dto.ChatResponse;
import com.amine.urticare.service.IChatService;
import jakarta.validation.Valid;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final IChatService chatService;

    public ChatController(IChatService chatService) {
        this.chatService = chatService;
    }

    /**
     * Public chatbot — no authentication required.
     *
     * POST /api/chat/message
     * Body: { "message": "...", "language": "fr" }
     */
    @PostMapping("/message")
    public ChatResponse publicMessage(@Valid @RequestBody ChatRequest request) {
        return chatService.sendPublicMessage(request);
    }

    /**
     * Authenticated chatbot — uses patient context.
     *
     * POST /api/chat/message/auth
     * Header: Authorization: Bearer <token>
     * Body: { "message": "...", "language": "fr" }
     */
    @PostMapping("/message/auth")
    public ChatResponse authenticatedMessage(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody ChatRequest request) {

        String userId = jwt.getSubject();
        return chatService.sendAuthenticatedMessage(userId, request);
    }
}

