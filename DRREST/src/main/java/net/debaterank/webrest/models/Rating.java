package net.debaterank.webrest.models;

import javax.persistence.*;

@Entity
@Table
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String uid; // not actually used by the calculation engine but useful to track whose rating is whose
    private double rating;
    private double ratingDeviation;
    private double volatility;
    private int numberOfResults = 0; // the number of results from which the rating has been calculated

    // the following variables are used to hold values temporarily whilst running calculations
    private double workingRating;
    private double workingRatingDeviation;
    private double workingVolatility;

    private String season;
    private String event;

    /**
     * Return the average skill value of the player.
     *
     * @return double
     */
    public double getRating() {
        return this.rating;
    }

    /**
     * @return Rating xxxx.xx
     */
    @Override
    public String toString() {
        return String.format("%02f", rating);
    }
}