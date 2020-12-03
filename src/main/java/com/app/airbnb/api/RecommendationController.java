package com.app.airbnb.api;

import com.app.airbnb.model.Accommodation;
import com.app.airbnb.model.Reservation;
import com.app.airbnb.model.Review;
import com.app.airbnb.model.User;
import com.app.airbnb.repositories.AccommodationRepository;
import com.app.airbnb.repositories.ReservationRepository;
import com.app.airbnb.repositories.UserRepository;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class RecommendationController {

    UserRepository userRepository;
    ReservationRepository reservationRepository;
    AccommodationRepository accommodationRepository;

    Timer timer = new Timer();
    private double[][] N = null;

    public RecommendationController(ReservationRepository reservationRepository, UserRepository userRepository,
                                    AccommodationRepository accommodationRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.accommodationRepository = accommodationRepository;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                matrixFactorization();
            }
        }, 0, 1000000);
    }

    @CrossOrigin(origins = "*")
    @GetMapping("/getRecommendations/{username}")
    List<Accommodation> getRecommendations(@PathVariable String username) {
        User user = this.userRepository.findByUsername(username);

        List<User> users = this.userRepository.findAll();
        List<Accommodation> accommodations = this.accommodationRepository.findAll();
        double[] sorted = null;

        int index = -1;
        if (this.N == null)
            return null;
        else {
            for (int i = 0; i < users.size(); i++) {
                if (users.get(i).getUsername().equals(user.getUsername())) {
                    index = i;

                    double[] ratings = N[index];
                    sorted = ratings.clone();
                    double temp;
                    Accommodation tempAcc;

                    for (int x = 0; x < sorted.length; x++) {
                        for (int y = x + 1; y < sorted.length; y++) {
                            if (sorted[x] < sorted[y]) {
                                temp = sorted[x];
                                sorted[x] = sorted[y];
                                sorted[y] = temp;

                                tempAcc = accommodations.get(x);
                                accommodations.set(x, accommodations.get(y));
                                accommodations.set(y, tempAcc);
                            }
                        }
                    }
                    break;
                }
            }
        }

        if (index == -1)
            return null;
        else {
            System.out.println("Recommendations for user: " + username);
            List<Accommodation> finalAccommodations = new ArrayList<>();
            for (int i = 0; i < 5 && i < accommodations.size(); i++) {
                System.out.println("Accommodation " + accommodations.get(i).getId() + " with rating: " + sorted[i]);
                finalAccommodations.add(accommodations.get(i));
            }
            return finalAccommodations;
        }
    }

    public double[][] randomMatrix(int x, int y) {
        double[][] m = new double[x][y];
        Random rand = new Random();

        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                m[i][j] = rand.nextDouble();
            }
        }

        return m;
    }

    public double[] getColumn(double[][] m, int index) {
        double[] column = new double[m.length];
        for (int i = 0; i < column.length; i++) {
            column[i] = m[i][index];
        }
        return column;
    }

    public double dotProduct(double[] row, double[] column) {
        double dot = 0.0;
        for (int i = 0; i < row.length; i++)
            dot += row[i] * column[i];
        return dot;
    }

    public double[][] multiplyMatrices(double[][] m1, double[][] m2, int x, int y, int k) {
        double[][] m = new double[x][y];
        for (int i = 0; i < x; i++) {
            for (int j = 0; j < y; j++) {
                for (int z = 0; z < k; z++) {
                    m[i][j] += m1[i][z] * m2[z][j];
                }
            }
        }
        return m;
    }

    public void matrixFactorization() {
        System.out.println("recommending...");
        List<User> users = this.userRepository.findAll();
        List<Accommodation> accommodations = this.accommodationRepository.findAll();

        int usersSize = users.size();
        int accommodationsSize = accommodations.size();

        double[][] X = new double[usersSize][accommodationsSize];
        for (int i = 0; i < usersSize; i++) {   //traverse through users

            List<Reservation> reservations = this.reservationRepository.findByGuestUsername(users.get(i).getUsername());
            List<Long> reservedAccommodations = new ArrayList<>();
            List<Review> reservedReviews = new ArrayList<>();

            for (Reservation reservation: reservations) {
                reservedAccommodations.add(reservation.getAccommodation().getId());
                reservedReviews.add(reservation.getReview());
            }

            // search history - doesn't work
            /*SearchHistory searchHistory = users.get(i).getSearchHistory();
            Set<Accommodation> searchAccommodations = searchHistory.getAccommodations();
            List<Long> searchIDs = new ArrayList<>();
            Set<Address> searchAddresses = searchHistory.getAddresses();*/

            //for (Accommodation accommodation: searchAccommodations)
            //    searchIDs.add(accommodation.getId());

            for (int j = 0; j < accommodationsSize; j++) {
                int index = reservedAccommodations.indexOf(accommodations.get(j).getId());
                if (index != -1) {  //this user has reserved this accommodation
                    if (reservedReviews.get(index) != null) //if user has added a review, add its rating to matrix
                        X[i][j] = reservedReviews.get(index).getRating();
                    else
                        X[i][j] = 0.0;
                }
                /*else {
                    index = searchIDs.indexOf(accommodations.get(j).getId());
                    if (index != -1)    //if user has clicked on this accommodation before
                        X[i][j] = 2.5;
                    else {
                        boolean found = false;
                        for (Address address: searchAddresses) {    //traverse through searched coordinates
                            double lat = address.getLat();
                            double lng = address.getLng();

                            double dist = this.accommodationRepository.calcDistance(lat, lng,
                                    accommodations.get(j).getLocation().getAddress().getLat(),
                                    accommodations.get(j).getLocation().getAddress().getLng());

                            if (dist < 50.0) {  //if distance is small enough
                                found = true;
                                X[i][j] = 2.5;
                                break;
                            }
                        }

                        if (!found) {
                            X[i][j] = 0.0;
                        }
                    }
                }*/
                else {
                    X[i][j] = 0.0;
                }
            }
        }

        this.N = algorithm(X, usersSize, accommodationsSize);
    }

    public double[][] algorithm(double[][] X, int usersSize, int accommodationsSize) {
        double eij;
        double learning_rate = 0.0002;
        double b = 0.02;    //regularization to avoid overfitting
        int k = 2;
        double rmse = Double.MAX_VALUE;

        double[][] V = randomMatrix(usersSize, k);
        double[][] F = randomMatrix(k, accommodationsSize);

        int iteration = 0;
        while (true) {
            for (int i = 0; i < usersSize; i++) {
                for (int j = 0; j < accommodationsSize; j++) {
                    if (X[i][j] > 0.0) {
                        double dotProduct = dotProduct(V[i], getColumn(F, j));

                        eij = X[i][j] - dotProduct;

                        // compute gradient + update (2aii + iii)

                        for (int z = 0; z < k; z++) {
                            double new_v = V[i][z] + learning_rate * (2 * eij * F[z][j] - b * V[i][z]);
                            double new_f = F[z][j] + learning_rate * (2 * eij * V[i][z] - b * F[z][j]);

                            V[i][z] = new_v;
                            F[z][j] = new_f;
                        }
                    }
                }
            }

            double e = 0.0;

            // calculate squared error

            for (int i = 0; i < usersSize; i++) {
                for (int j = 0; j < accommodationsSize; j++) {
                    if (X[i][j] > 0.0) {
                        double dotProduct = dotProduct(V[i], getColumn(F, j));
                        e = e + Math.pow(X[i][j] - dotProduct, 2);
                        for (int z = 0; z < k; z++) {
                            e = e + (b / 2) * (Math.pow(V[i][z], 2) + Math.pow(F[z][j], 2));
                        }
                    }
                }
            }

            iteration++;

            if (rmse <= e) {
                //System.out.println(rmse);
                //System.out.println(e);
                //System.out.println(iteration);
                break;
            }
            else
                rmse = e;
        }

        return multiplyMatrices(V, F, usersSize, accommodationsSize, k);
    }
}
