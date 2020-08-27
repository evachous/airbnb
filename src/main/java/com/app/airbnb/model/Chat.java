package com.app.airbnb.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
public class Chat {
    @Id @GeneratedValue private Long id;

    private Boolean guestRead;
    private Boolean hostRead;

    @ManyToOne
    //@JsonIgnoreProperties("chats")
    @JoinColumn(name = "accommodation_id")
    private Accommodation accommodation;

    @ManyToOne
    //@JsonIgnoreProperties("chats")
    @JoinColumn(name = "user_id")
    private User guest;

    @OneToMany(mappedBy = "chat")
    @JsonIgnoreProperties("chat")
    private List<ChatMessage> messages;

    public Chat() {}

    public Chat(Accommodation accommodation, User guest) {
        this.accommodation = accommodation;
        this.guest = guest;
        this.guestRead = true;
        this.hostRead = false;
    }
}
