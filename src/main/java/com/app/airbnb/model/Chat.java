package com.app.airbnb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Chat {
    @Id @GeneratedValue private Long id;

    @OneToMany(mappedBy = "chat")
    @JsonIgnoreProperties("chat")
    private List<ChatMessage> messages;

    private Boolean guestRead;
    private Boolean hostRead;

    @ManyToOne(cascade=CascadeType.ALL)
    @JsonIgnoreProperties("chats")
    private Accommodation accommodation;

    @ManyToOne(cascade=CascadeType.ALL)
    @JsonIgnoreProperties("chats")
    private User guest;

    public Chat() {}

    public Chat(Accommodation accommodation, User guest) {
        this.accommodation = accommodation;
        this.guest = guest;
        this.guestRead = true;
        this.hostRead = false;
    }
}
