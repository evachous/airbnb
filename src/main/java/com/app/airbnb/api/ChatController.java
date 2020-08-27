package com.app.airbnb.api;

import com.app.airbnb.model.*;
import com.app.airbnb.repositories.AccommodationRepository;
import com.app.airbnb.repositories.ChatMessageRepository;
import com.app.airbnb.repositories.ChatRepository;
import com.app.airbnb.repositories.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

@RestController
class ChatController {
    ChatRepository chatRepository;
    ChatMessageRepository chatMessageRepository;
    AccommodationRepository accommodationRepository;
    UserRepository userRepository;

    ChatController(ChatRepository chatRepository, ChatMessageRepository chatMessageRepository,
                   AccommodationRepository accommodationRepository, UserRepository userRepository) {
        this.chatRepository = chatRepository;
        this.chatMessageRepository = chatMessageRepository;
        this.accommodationRepository = accommodationRepository;
        this.userRepository = userRepository;
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/createChat")
    ResponseEntity<String> createChat(@RequestParam("accommodationID") String accommodationID,
                                      @RequestParam("guestUsername") String guestUsername) {
        User guest = this.userRepository.findByUsername(guestUsername);
        if (this.chatRepository.findByAccommodationAndGuest(Long.parseLong(accommodationID), guestUsername) == null) {
            Accommodation accommodation = this.accommodationRepository.getOne(Long.parseLong(accommodationID));
            Chat chat = new Chat(accommodation, guest);
            this.chatRepository.save(chat);

            /*List<Chat> accommodationChats = accommodation.getChats();
            accommodationChats.add(chat);
            accommodation.setChats(accommodationChats);
            this.accommodationRepository.save(accommodation);*/

            /*List<Chat> guestChats = guest.getChats();
            guestChats.add(chat);
            guest.setChats(guestChats);
            this.userRepository.save(guest);*/
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getAccommodationChats/{accommodationID}")
    List<Chat> returnAccommodationChats(@PathVariable Long accommodationID) {
        return this.chatRepository.findByAccommodation(accommodationID);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getHostChats/{username}")
    List<Chat> returnHostChats(@PathVariable String username) {
        return this.chatRepository.findByHostUsername(username);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getGuestChats/{username}")
    List<Chat> returnGuestChats(@PathVariable String username) {
        return this.chatRepository.findByGuestUsername(username);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getChat/{accommodationID}/{guestUsername}/{currentUsername}")
    Chat returnChat(@PathVariable Long accommodationID, @PathVariable String guestUsername,
                    @PathVariable String currentUsername) {
        Chat chat = this.chatRepository.findByAccommodationAndGuest(accommodationID, guestUsername);
        User user = this.userRepository.findByUsername(currentUsername);
        if (chat == null) {
            throw new ChatNotFoundException();
        }

        if (user.getIsGuest()) {
            chat.setGuestRead(true);
        }
        else if (user.getIsHost()) {
            chat.setHostRead(true);
        }

        this.chatRepository.save(chat);
        return chat;
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/chatCheck/{accommodationID}/{guestUsername}/{currentUsername}")
    boolean chatCheck(@PathVariable Long accommodationID, @PathVariable String guestUsername,
                      @PathVariable String currentUsername) {
        Chat chat = this.chatRepository.findByAccommodationAndGuest(accommodationID, guestUsername);
        if (chat == null) {
            return false;
        }
        return (chat.getAccommodation().getHost().getUsername().equals(currentUsername) ||
                guestUsername.equals(currentUsername));
    }

    @CrossOrigin(origins = "*")
    @PostMapping("/sendMessage")
    ResponseEntity<String> sendMessage(@RequestParam("chatID") String chatID, @RequestParam("senderUsername") String senderUsername,
                                       @RequestParam("message") String message) {
        Chat chat = this.chatRepository.getOne(Long.parseLong(chatID));
        User user = this.userRepository.findByUsername(senderUsername);

        Timestamp timestamp = new Timestamp(new Date().getTime());
        ChatMessage chatMessage = new ChatMessage(chat, senderUsername, message, timestamp);

        this.chatMessageRepository.save(chatMessage);

        List<ChatMessage> chatMessages = chat.getMessages();
        chatMessages.add(chatMessage);
        chat.setMessages(chatMessages);

        if (user.getIsGuest()) {
            chat.setGuestRead(true);
            chat.setHostRead(false);
        }
        else if (user.getIsHost()) {
            chat.setGuestRead(false);
            chat.setHostRead(true);
        }

        this.chatRepository.save(chat);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @CrossOrigin(origins = "*")
    @DeleteMapping("/deleteChat/{id}")
    ResponseEntity<String> deleteChat(@PathVariable Long id) {
        Chat chat = this.chatRepository.getOne(id);
        //System.out.println(chat.getId());
        //System.out.println(chat.getAccommodation().getId());
        List<ChatMessage> messages = chat.getMessages();

        for (ChatMessage message : messages) {
            this.chatMessageRepository.deleteById(message.getId());
        }

        /*Accommodation accommodation = chat.getAccommodation();
        List<Chat> accommodationChats = accommodation.getChats();
        accommodationChats.remove(chat);
        this.accommodationRepository.save(accommodation);

        User guest = chat.getGuest();
        List<Chat> guestChats = guest.getChats();
        guestChats.remove(chat);
        this.userRepository.save(guest);*/

        //this.chatRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
