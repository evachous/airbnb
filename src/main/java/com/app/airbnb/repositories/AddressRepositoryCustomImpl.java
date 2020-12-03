package com.app.airbnb.repositories;

import com.app.airbnb.model.Address;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class AddressRepositoryCustomImpl implements AddressRepositoryCustom {

    @PersistenceContext
    EntityManager entityManager;

    public Address findByCoordinates(Double lat, Double lng) {
        Address address = null;
        Query query = entityManager.createQuery("SELECT a FROM Address a WHERE a.lat = ?1 AND a.lng = ?2");
        query.setParameter(1, lat);
        query.setParameter(2, lng);
        List<Address> addresses = query.getResultList();
        if (addresses != null && addresses.size() > 0)
            address = addresses.get(0);
        return address;
    }
}
