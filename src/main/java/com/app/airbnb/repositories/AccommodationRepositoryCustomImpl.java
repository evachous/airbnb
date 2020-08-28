package com.app.airbnb.repositories;

import com.app.airbnb.model.*;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class AccommodationRepositoryCustomImpl implements AccommodationRepositoryCustom {

    public double calcDistance(double lat1, double lng1, double lat2, double lng2) {
        if ((lat1 == lat2) && (lng1 == lng2)) {
            return 0.0;
        }
        else {
            double theta = lng1 - lng2;
            double dist = Math.sin(Math.toRadians(lat1)) * Math.sin(Math.toRadians(lat2)) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.cos(Math.toRadians(theta));
            dist = Math.acos(dist);
            dist = Math.toDegrees(dist);
            dist = dist * 60 * 1.1515 * 1.609344;
            return dist;
        }
    }

    public void replaceAccommodationInfo(Accommodation accommodation, AccommodationInfo newInfo) {
        AccommodationInfo info = accommodation.getInfo();
        info.setName(newInfo.getName());
        info.setStartDate(newInfo.getStartDate());
        info.setEndDate(newInfo.getEndDate());
        info.setMinCost(newInfo.getMinCost());
        info.setCostPerPerson(newInfo.getCostPerPerson());
        info.setType(newInfo.getType());
        info.setBeds(newInfo.getBeds());
        info.setBedrooms(newInfo.getBedrooms());
        info.setBathrooms(newInfo.getBathrooms());
        info.setLivingRoom(newInfo.getLivingRoom());
        info.setArea(newInfo.getArea());
        info.setDescription(newInfo.getDescription());
        info.setInternet(newInfo.getInternet());
        info.setAc(newInfo.getAc());
        info.setHeating(newInfo.getHeating());
        info.setKitchen(newInfo.getKitchen());
        info.setTv(newInfo.getTv());
        info.setParking(newInfo.getParking());
        info.setElevator(newInfo.getElevator());
    }

    public void replaceAccommodationLocation(Accommodation accommodation, AccommodationLocation newLocation) {
        AccommodationLocation location = accommodation.getLocation();
        location.setTransportation(newLocation.getTransportation());

        Address address = location.getAddress();
        Address newAddress = newLocation.getAddress();
        address.setLabel(newAddress.getLabel());
        address.setNumber(newAddress.getNumber());
        address.setRoad(newAddress.getRoad());
        address.setSuburb(newAddress.getSuburb());
        address.setCity(newAddress.getCity());
        address.setState(newAddress.getState());
        address.setPostcode(newAddress.getPostcode());
        address.setCountry(newAddress.getCountry());
        address.setLat(newAddress.getLat());
        address.setLng(newAddress.getLng());
    }

    public void replaceAccommodationRules(Accommodation accommodation, AccommodationRules newRules) {
        AccommodationRules rules = accommodation.getRules();
        rules.setSmoking(newRules.getSmoking());
        rules.setPets(newRules.getPets());
        rules.setEvents(newRules.getEvents());
        rules.setMinDays(newRules.getMinDays());
        rules.setMaxPeople(newRules.getMaxPeople());
    }
}
