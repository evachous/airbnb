package com.app.airbnb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Chat {
    @Id @GeneratedValue private Long id;

    @ManyToOne(cascade=CascadeType.ALL)
    @JsonIgnoreProperties("chats")
    private Accommodation accommodation;

    private String guestUsername;

    @OneToMany(mappedBy = "chat")
    @JsonIgnoreProperties("chat")
    private List<ChatMessage> messages;

    public Chat() {}

    public Chat(Accommodation accommodation, String guestUsername) {
        this.accommodation = accommodation;
        this.guestUsername = guestUsername;
    }
}
