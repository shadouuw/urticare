package com.amine.urticare.model.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "chat_messages", schema = "public")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "user_message", columnDefinition = "TEXT", nullable = false)
    private String userMessage;

    @Column(name = "bot_reply", columnDefinition = "TEXT", nullable = false)
    private String botReply;

    @Column(nullable = false, length = 5)
    private String language;

    @Column(name = "is_authenticated", nullable = false)
    private Boolean isAuthenticated = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }


}