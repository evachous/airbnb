package com.app.airbnb.repositories;

import com.app.airbnb.model.Chat;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class ChatRepositoryCustomImpl implements ChatRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public List<Chat> findByAccommodation(Long accommodationID) {
        Query query = entityManager.createQuery("SELECT c FROM Chat c WHERE c.accommodation.id = ?1");
        query.setParameter(1, accommodationID);
        return query.getResultList();
    }

    @Override
    public List<Chat> findByHostUsername(String username) {
        Query query = entityManager.createQuery("SELECT c FROM Chat c WHERE c.accommodation.host.username = ?1");
        query.setParameter(1, username);
        return query.getResultList();
    }

    @Override
    public List<Chat> findByGuestUsername(String username) {
        Query query = entityManager.createQuery("SELECT c FROM Chat c WHERE c.guestUsername = ?1");
        query.setParameter(1, username);
        return query.getResultList();
    }

    @Override
    public Chat findByAccommodationAndGuest(Long accommodationID, String guestUsername) {
        Chat chat = null;
        Query query = entityManager.createQuery("SELECT c FROM Chat c WHERE c.accommodation.id = ?1 AND c.guestUsername = ?2");
        query.setParameter(1, accommodationID);
        query.setParameter(2, guestUsername);
        List<Chat> chats = query.getResultList();
        if (chats != null && chats.size() > 0)
            chat = chats.get(0);
        return chat;
    }
}
