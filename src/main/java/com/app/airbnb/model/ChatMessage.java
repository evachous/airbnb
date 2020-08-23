package com.app.airbnb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
public class ChatMessage {
    @Id @GeneratedValue private Long id;

    @ManyToOne(cascade=CascadeType.ALL)
    @JsonIgnoreProperties("messages")
    private Chat chat;

    private String senderUsername;
    private String message;
    private Timestamp timestamp;

    public ChatMessage() {}

    public ChatMessage(Chat chat, String senderUsername, String message, Timestamp timestamp) {
        this.chat = chat;
        this.senderUsername = senderUsername;
        this.message = message;
        this.timestamp = timestamp;
    }
}
