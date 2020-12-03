package com.app.airbnb.repositories;

import com.app.airbnb.model.SearchHistory;

public interface SearchHistoryRepositoryCustom {
    SearchHistory findByUsername(String username);
}
