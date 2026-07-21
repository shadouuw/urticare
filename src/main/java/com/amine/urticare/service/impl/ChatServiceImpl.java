
package com.amine.urticare.service.impl;

import com.amine.urticare.model.entities.ChatMessage;
import com.amine.urticare.repository.ChatMessageRepository;
import com.amine.urticare.resources.dto.ChatRequest;
import com.amine.urticare.resources.dto.ChatResponse;
import com.amine.urticare.service.IChatService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.List;

/**
 * Chatbot backed by Google's Gemini API (free tier via an AI Studio API key).
 * The assistant is explicitly instructed to never act as a doctor — only
 * general educational information, always closing with a reminder to consult
 * a real healthcare professional.
 */
@Service
public class ChatServiceImpl implements IChatService {

    private static final String GEMINI_URL_TEMPLATE =
            "https://generativelanguage.googleapis.com/v1beta/models/%s:generateContent?key=%s";

    private final ChatMessageRepository chatMessageRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(10))
            .build();

    @Value("${gemini.api-key:}")
    private String geminiApiKey;

    @Value("${gemini.model:gemini-flash-latest}")
    private String geminiModel;

    public ChatServiceImpl(ChatMessageRepository chatMessageRepository) {
        this.chatMessageRepository = chatMessageRepository;
    }

    @Override
    public ChatResponse sendPublicMessage(ChatRequest request) {
        String reply = generateReply(request.getMessage(), request.getLanguage());

        ChatMessage msg = new ChatMessage();
        msg.setUserMessage(request.getMessage());
        msg.setBotReply(reply);
        msg.setLanguage(request.getLanguage());
        msg.setIsAuthenticated(false);
        chatMessageRepository.save(msg);

        return new ChatResponse(reply);
    }

    @Override
    public ChatResponse sendAuthenticatedMessage(String userId, ChatRequest request) {
        String reply = generateReply(request.getMessage(), request.getLanguage());

        ChatMessage msg = new ChatMessage();
        msg.setUserId(userId);
        msg.setUserMessage(request.getMessage());
        msg.setBotReply(reply);
        msg.setLanguage(request.getLanguage());
        msg.setIsAuthenticated(true);
        chatMessageRepository.save(msg);

        return new ChatResponse(reply);
    }

    // --------------------------- Gemini integration ---------------------------

    private String generateReply(String message, String language) {
        if (geminiApiKey == null || geminiApiKey.isBlank()) {
            return fallbackReply(language);
        }

        try {
            GeminiRequest body = new GeminiRequest(
                    new GeminiSystemInstruction(List.of(new GeminiPart(systemPrompt(language)))),
                    List.of(new GeminiContent(List.of(new GeminiPart(message))))
            );

            HttpRequest httpRequest = HttpRequest.newBuilder()
                    .uri(URI.create(GEMINI_URL_TEMPLATE.formatted(geminiModel, geminiApiKey)))
                    .header("Content-Type", "application/json")
                    .timeout(Duration.ofSeconds(20))
                    .POST(HttpRequest.BodyPublishers.ofString(
                            objectMapper.writeValueAsString(body), StandardCharsets.UTF_8))
                    .build();

            HttpResponse<String> response = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() != 200) {
                return fallbackReply(language);
            }

            JsonNode root = objectMapper.readTree(response.body());
            JsonNode textNode = root.path("candidates").path(0).path("content").path("parts").path(0).path("text");

            if (textNode.isMissingNode() || textNode.asText().isBlank()) {
                return fallbackReply(language);
            }

            return textNode.asText().trim();

        } catch (Exception e) {
            return fallbackReply(language);
        }
    }

    private String systemPrompt(String language) {
        String languageName = switch (language) {
            case "en" -> "English";
            case "ar" -> "Arabic";
            default -> "French";
        };

        return """
                You are the UrtiCare Assistant, a health-information chatbot inside a chronic \
                urticaria (hives) tracking app. You are NOT a doctor, nurse, or any kind of \
                medical professional, and you must never present yourself as one.

                Rules you must always follow:
                1. Only provide general, educational information about urticaria/hives, common \
                   treatments, triggers, skin care, and related wellbeing topics, based on \
                   widely accepted public medical knowledge.
                2. Never diagnose a specific person, never prescribe or adjust medication doses, \
                   and never claim certainty about what is happening to the user personally.
                3. Always end every single response with a clear reminder to consult a real \
                   doctor or qualified healthcare professional for diagnosis, treatment changes, \
                   or anything specific to their situation.
                4. If the message describes a possible emergency (e.g. difficulty breathing, \
                   swelling of the face/throat/tongue, dizziness, fainting), tell them to seek \
                   emergency medical care immediately, before anything else.
                5. Keep answers concise, warm, and easy to understand for a non-medical reader.
                6. Reply in %s, matching the user's language.
                """.formatted(languageName);
    }

    private String fallbackReply(String language) {
        return switch (language) {
            case "en" -> "I'm having trouble answering right now. For any question about your urticaria, please check with your doctor.";
            case "ar" -> "أواجه مشكلة في الإجابة الآن. لأي سؤال بخصوص الشرى لديك، يرجى استشارة طبيبك.";
            default -> "Je rencontre un problème pour répondre pour le moment. Pour toute question sur ton urticaire, consulte ton médecin.";
        };
    }

    // --------------------------- Gemini request shape ---------------------------

    private record GeminiPart(String text) {}

    private record GeminiContent(List<GeminiPart> parts) {}

    private record GeminiSystemInstruction(List<GeminiPart> parts) {}

    private record GeminiRequest(GeminiSystemInstruction systemInstruction, List<GeminiContent> contents) {}
}
