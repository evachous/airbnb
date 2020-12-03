package com.app.airbnb.bonus;

import com.app.airbnb.model.*;
import com.app.airbnb.repositories.*;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.parameters.P;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class MatrixFactorization {
    private final ReservationRepository reservationRepository;
    private final UserRepository userRepository;
    private final AccommodationRepository accommodationRepository;

    public double[][] N;

    Timer timer = new Timer();

    public MatrixFactorization(ReservationRepository reservationRepository, UserRepository userRepository,
                               AccommodationRepository accommodationRepository) {
        this.reservationRepository = reservationRepository;
        this.userRepository = userRepository;
        this.accommodationRepository = accommodationRepository;

        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                recommend();
            }
        }, 0, 10000000);
    }

    public void readCSV() {
        System.out.println("Start of CSV...");

        String csvFile = "C://temp/reviews.csv";
        String line;
        String cvsSplitBy = ",";

        NLP.init();

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {

            CSVReader reader = new CSVReader(new FileReader(csvFile));
            String [] nextLine;
            int lineNumber = 0;
            while ((nextLine = reader.readNext()) != null) {
                lineNumber++;
                System.out.println("Line # " + lineNumber);

                System.out.println(nextLine[5]);
            }

            /*List<String> allAccommodations = new ArrayList<>();
            List<String> allUsers = new ArrayList<>();
            List<Double> ratings = new ArrayList<>();

            int iterator = 0;
            line = br.readLine();
            String[] title = line.split(cvsSplitBy);

            //while ((line = br.readLine()) != null && iterator < 209) {
            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] review = line.split(cvsSplitBy);

                String comment = "";
                for (int i = 5; i < review.length; i++) {
                    comment = comment.concat(review[i]);
                }
                if ((comment.startsWith("\"") && !comment.endsWith("\"")) || (comment.startsWith("\"") && comment.length() == 1)) {
                    while ((line = br.readLine()) != null) {
                        comment = comment.concat(" " + line);
                        if (line.endsWith("\""))
                            break;
                    }
                }

                ratings.add((double) NLP.findSentiment(comment));

                if (!allAccommodations.contains(review[0]))
                    accommodations.add(review[0]);
                allAccommodations.add(review[0]);

                if (!allUsers.contains(review[3]))
                    users.add(review[3]);
                allUsers.add(review[3]);

                iterator++;

                if (iterator == 209)
                    break;
            }

            System.out.println(iterator);

            int usersSize = users.size();
            int accommodationsSize = accommodations.size();

            double[][] X = new double[usersSize][accommodationsSize];
            for (int i = 0; i < ratings.size(); i++) {
                int accommodationIndex = accommodations.indexOf(allAccommodations.get(i));
                int usersIndex = users.indexOf(allUsers.get(i));
                X[usersIndex][accommodationIndex] = ratings.get(i);
                //System.out.println(i + " " + allAccommodations.get(i) + " " + allUsers.get(i) + " " + ratings.get(i));
            }

            this.N = algorithm(X, usersSize, accommodationsSize);

            int index = users.indexOf("33278513");
            double[] finalRatings = N[index];
            double[] sorted = finalRatings.clone();
            double temp;
            String tempAcc;

            for (int j = 0; j < finalRatings.length; j++)
                System.out.println(finalRatings[j] + " " + accommodations.get(j));

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

            for (int j = 0; j < sorted.length; j++)
                System.out.println(sorted[j] + " " + accommodations.get(j));*/

        } catch (IOException | CsvValidationException e) {
            e.printStackTrace();
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
        //System.out.println(row.length + " " + column.length);
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

    public void recommend() {
        List<User> users = this.userRepository.findAll();
        List<Accommodation> accommodations = this.accommodationRepository.findAll();

        int usersSize = users.size();
        int accommodationsSize = accommodations.size();

        double[][] X = new double[usersSize][accommodationsSize];
        for (int i = 0; i < usersSize; i++) {

            List<Reservation> reservations = this.reservationRepository.findByGuestUsername(users.get(i).getUsername());
            List<Long> reservedAccommodations = new ArrayList<>();
            List<Review> reservedReviews = new ArrayList<>();

            for (Reservation reservation: reservations) {
                reservedAccommodations.add(reservation.getAccommodation().getId());
                //System.out.println("Reservation " + users.get(i).getUsername()+ " " + reservation.getAccommodation().getId());
                reservedReviews.add(reservation.getReview());
            }

            /*SearchHistory searchHistory = users.get(i).getSearchHistory();
            List<Accommodation> searchAccommodations = new ArrayList<>();
            List<Long> searchIDs = new ArrayList<>();
            List<Address> searchAddresses = new ArrayList<>();*/
            //Set<Address> searchAddresses = searchHistory.getAddresses();

            //for (Accommodation accommodation: searchAccommodations)
            //    searchIDs.add(accommodation.getId());

            for (int j = 0; j < accommodationsSize; j++) {
                int index = reservedAccommodations.indexOf(accommodations.get(j).getId());
                if (index != -1) {
                    if (reservedReviews.get(index) != null)
                        X[i][j] = reservedReviews.get(index).getRating();
                    else
                        X[i][j] = 0.0;
                }
                /*else {
                    index = searchIDs.indexOf(accommodations.get(j).getId());
                    if (index != -1)
                        X[i][j] = 2.5;
                    else {
                        boolean found = false;
                        for (Address address: searchAddresses) {
                            System.out.println(address.getId());
                            double lat = address.getLat();
                            double lng = address.getLng();

                            double dist = this.accommodationRepository.calcDistance(lat, lng,
                                    accommodations.get(j).getLocation().getAddress().getLat(),
                                    accommodations.get(j).getLocation().getAddress().getLng());

                            if (dist < 50.0) {
                                found = true;
                                X[i][j] = 2.5;
                                break;
                            }
                        }

                        if (!found) {
                            System.out.println("HIIII");
                            X[i][j] = 0.0;
                        }
                    }
                }*/
                /*else {
                    X[i][j] = 0.0;
                }*/
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

            //System.out.println(e);
            iteration++;
            //if (e < 0.001)
            //    break;
            //if (iteration == 1000)
            //    System.out.println(e);

            if (rmse <= e) {
                System.out.println(rmse);
                System.out.println(e);
                System.out.println(iteration);
                break;
            }
            else
                rmse = e;

            //if (iteration % 100000 == 0)
            //    System.out.println("iteration: " + iteration + " " + rmse);
        }

        return multiplyMatrices(V, F, usersSize, accommodationsSize, k);
    }

    @Bean
    public List<Accommodation> getRecommendations(User user) {
        List<User> users = this.userRepository.findAll();
        List<Accommodation> accommodations = this.accommodationRepository.findAll();

        int index = -1;
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getUsername().equals(user.getUsername())) {
                index = i;

                double[] ratings = N[index];
                double[] sorted = ratings.clone();
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

                for (int j = 0; j < sorted.length; j++) {
                    System.out.println(sorted[j] + " " + accommodations.get(j).getId());
                }
                break;
            }
        }

        if (index == -1)
            return null;
        else {
            List<Accommodation> finalAccommodations = new ArrayList<>();
            for (int i = 0; i < 5 && i < accommodations.size(); i++)
                finalAccommodations.add(accommodations.get(i));
            return finalAccommodations;
        }
    }
}
