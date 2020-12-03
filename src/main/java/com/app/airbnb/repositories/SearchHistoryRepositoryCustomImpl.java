package com.app.airbnb.repositories;

import com.app.airbnb.model.SearchHistory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class SearchHistoryRepositoryCustomImpl implements SearchHistoryRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    public SearchHistory findByUsername(String username) {
        SearchHistory searchHistory = null;
        Query query = entityManager.createQuery("SELECT s FROM SearchHistory s WHERE s.guest.username = ?1");
        query.setParameter(1, username);
        List<SearchHistory> searchHistories = query.getResultList();
        if (searchHistories != null && searchHistories.size() > 0)
            searchHistory = searchHistories.get(0);
        return searchHistory;
    }
}
