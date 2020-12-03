package com.app.airbnb.model;

import com.app.airbnb.api.RecommendationController;
import com.app.airbnb.bonus.MatrixFactorization;
import com.app.airbnb.bonus.NLP;
import com.app.airbnb.repositories.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

@Configuration
@Slf4j
class LoadDatabase {

    @Bean
    CommandLineRunner initDatabase(ReservationRepository reservationRepository, UserRepository userRepository,
                                   AccommodationRepository accommodationRepository, ImageRepository imageRepository,
                                   ReviewRepository reviewRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        return args -> {
            //String encodedPassword = bCryptPasswordEncoder.encode("admin");
            //log.info("Preloading " + userRepository.save(new User("admin", encodedPassword, "admin", "admin", "admin@example.com", "1234567890", "london", "uk", true, false, false, true)));
            //loadListings(userRepository, accommodationRepository, imageRepository);
            //loadReviews(userRepository, accommodationRepository, reservationRepository, reviewRepository);
            RecommendationController recommendationController = new RecommendationController(reservationRepository, userRepository, accommodationRepository);
        };
    }

    public void loadListings(UserRepository userRepository, AccommodationRepository accommodationRepository,
                             ImageRepository imageRepository) {
        String csvFile = "C://temp/listings.csv";

        try {
            CSVReader reader = new CSVReader(new FileReader(csvFile));
            Random rand = new Random();
            String [] line;
            line = reader.readNext();
            int lineNumber = 1;

            // even distribution of listings among hosts
            List<User> hosts = userRepository.findAllHosts();
            int i = 0;

            // read listings from csv line by line
            while ((line = reader.readNext()) != null && lineNumber <= 70) {
                lineNumber++;
                System.out.println("Line # " + lineNumber);

                // set accommodation info
                AccommodationInfo info = new AccommodationInfo();
                info.setName(line[4]);

                if (!line[56].isEmpty())
                    info.setMinCost(Double.parseDouble(line[56].substring(1)));

                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("u-M-d");
                LocalDate localStartDate = LocalDate.parse("2000-01-01", dateFormatter);
                LocalDate localEndDate = LocalDate.parse("2021-01-01", dateFormatter);
                info.setStartDate(localStartDate);
                info.setEndDate(localEndDate);

                info.setType(line[48]);

                if (line[52].isEmpty())
                    info.setBeds(0);
                else
                    info.setBeds(Integer.parseInt(line[52]));

                if (line[51].isEmpty())
                    info.setBedrooms(0);
                else
                    info.setBedrooms(Integer.parseInt(line[51]));

                if (line[50].isEmpty())
                    info.setBathrooms(0.0);
                else
                    info.setBathrooms(Double.parseDouble(line[50]));

                info.setLivingRoom(rand.nextBoolean());

                if (line[55].isEmpty())
                    info.setArea(0);
                else
                    info.setArea(Integer.parseInt(line[55]));

                if (line[7].length() >= 500)
                    info.setDescription(line[7].substring(0, 500));
                else
                    info.setDescription(line[7]);

                String amenities_str = line[54].substring(1, line[54].length()-1);
                String[] amenities = amenities_str.split(",");
                boolean pets = false, smoking = false, events = false;
                info.setInternet(false);
                info.setAc(false);
                info.setHeating(false);
                info.setKitchen(false);
                info.setTv(false);
                info.setParking(false);
                info.setElevator(false);
                for (String amenity: amenities) {
                    if (amenity.equals("Internet")) info.setInternet(true);
                    if (amenity.equals("\"Air Conditioning\"")) info.setAc(true);
                    if (amenity.equals("Heating")) info.setHeating(true);
                    if (amenity.equals("Kitchen")) info.setKitchen(true);
                    if (amenity.equals("TV")) info.setTv(true);
                    if (amenity.equals("\"Free Parking on Premises\"")) info.setParking(true);
                    if (amenity.equals("\"Elevator in Building\"")) info.setElevator(true);
                    if (amenity.equals("\"Pets Allowed\"")) pets = true;
                    if (amenity.equals("\"Smoking Allowed\"")) smoking = true;
                    if (amenity.equals("\"Suitable for Events\"")) events = true;
                }

                // set accommodation location
                Address address = new Address();
                address.setLabel(info.getName());
                address.setRoad(line[34]);
                if (line[35].isEmpty())
                    address.setSuburb(null);
                else
                    address.setSuburb(line[35]);
                address.setCity(line[38]);
                if (line[39].isEmpty())
                    address.setState(null);
                else
                    address.setState(line[39]);
                if (line[40].isEmpty())
                    address.setPostcode(null);
                else
                    address.setPostcode(line[40]);
                address.setCountry(line[43]);
                address.setLat(Double.parseDouble(line[44]));
                address.setLng(Double.parseDouble(line[45]));

                AccommodationLocation location = new AccommodationLocation();
                if (line[11].isEmpty())
                    location.setTransportation(null);
                else if (line[11].length() >= 250)
                    location.setTransportation(line[11].substring(0, 250));
                else
                    location.setTransportation(line[11]);
                location.setAddress(address);
                System.out.println(location.getId());

                // set accommodation rules
                AccommodationRules rules = new AccommodationRules();
                rules.setSmoking(smoking);
                rules.setPets(pets);
                rules.setEvents(events);
                if (!line[63].isEmpty())
                    rules.setMinDays(Integer.parseInt(line[63]));
                if (!line[64].isEmpty())
                    rules.setMaxDays(Integer.parseInt(line[64]));
                if (!line[49].isEmpty()) {
                    rules.setMaxPeople(Integer.parseInt(line[49]));
                    info.setCostPerPerson(info.getMinCost() / rules.getMaxPeople());
                }

                User host = hosts.get(i);
                Accommodation accommodation = new Accommodation();
                accommodationRepository.save(accommodation);

                accommodation.setDatasetId(Long.parseLong(line[0]));
                accommodation.setInfo(info);
                accommodation.setLocation(location);
                accommodation.setRules(rules);
                accommodation.setHost(host);
                accommodation.setNumRatings(0);
                accommodation.setAvgRating(0.0);
                System.out.println(accommodation.getId());

                String path = userRepository.uploadImageByURL(line[13], accommodation.getDatasetId().toString(), "accommodation");
                if (path != null) {
                    Image newImage = new Image();
                    imageRepository.save(newImage);
                    newImage.setPath(path);
                    newImage.setAccommodation(accommodation);
                    imageRepository.save(newImage);
                }

                accommodationRepository.save(accommodation);

                List<Accommodation> accommodations = host.getAccommodations();
                accommodations.add(accommodation);
                host.setAccommodations(accommodations);
                userRepository.save(host);

                i++;
                if (i == hosts.size()) i = 0;
            }
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }

    public void loadReviews(UserRepository userRepository, AccommodationRepository accommodationRepository,
                            ReservationRepository reservationRepository, ReviewRepository reviewRepository) {
        String csvFile = "C://temp/reviews.csv";

        try {
            CSVReader reader = new CSVReader(new FileReader(csvFile));
            Random rand = new Random();
            String [] line;
            line = reader.readNext();
            int lineNumber = 1;

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("u-M-d");
            LocalDate startDate = LocalDate.parse("2005-01-01", dateFormatter);
            LocalDate endDate = LocalDate.parse("2005-01-02", dateFormatter);

            // even distribution of reviews among hosts
            List<User> guests = userRepository.findAllGuests();
            int i = 0;

            NLP.init();

            // read reviews from csv line by line
            while ((line = reader.readNext()) != null) {
                lineNumber++;
                System.out.println("Line # " + lineNumber);

                Accommodation accommodation = accommodationRepository.findByDatasetId(Long.parseLong(line[0]));

                if (accommodation != null) {
                    User guest = guests.get(i);

                    Reservation reservation = new Reservation();
                    reservation.setGuest(guest);
                    reservation.setAccommodation(accommodation);
                    reservation.setNumPeople(rand.nextInt(accommodation.getRules().getMaxPeople() - 1) + 1);
                    reservation.setStartDate(startDate);
                    reservation.setEndDate(endDate);
                    reservation.setDays((long)2);

                    reservationRepository.save(reservation);

                    List<Review> accommodationReviews = reviewRepository.findByAccommodation(accommodation.getId());

                    User host = accommodation.getHost();
                    List<Review> hostReviews = reviewRepository.findByHostUsername(host.getUsername());

                    Review review = new Review();
                    review.setReservation(reservation);
                    String text;
                    if (line[5].length() >= 250)
                        text = line[5].substring(0, 250);
                    else
                        text = line[5];
                    review.setText(text);
                    review.setRating((double) NLP.findSentiment(text));

                    reviewRepository.save(review);

                    reservation.setReview(review);
                    reservationRepository.save(reservation);

                    accommodation.setNumRatings(accommodation.getNumRatings() + 1);
                    double total = review.getRating();
                    for (Review accommodationReview : accommodationReviews) {
                        total = total + accommodationReview.getRating();
                    }
                    accommodation.setAvgRating(total / accommodation.getNumRatings());

                    host.setNumRatings(host.getNumRatings() + 1);
                    total = review.getRating();
                    for (Review hostReview : hostReviews) {
                        total = total + hostReview.getRating();
                    }
                    host.setAvgRating(total / host.getNumRatings());

                    reservationRepository.save(reservation);
                    accommodationRepository.save(accommodation);
                    userRepository.save(host);

                    startDate = startDate.plusDays(2);
                    endDate = endDate.plusDays(2);

                    i++;
                    if (i == guests.size()) i = 0;
                }
            }
            System.out.println(lineNumber);
        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
        }
    }
}
