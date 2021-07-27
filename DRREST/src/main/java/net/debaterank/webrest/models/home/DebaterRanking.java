package net.debaterank.webrest.models.home;

public class DebaterRanking {
    public int key; // For React
    public int ranking; // #1, #2, ...
    public int numRounds;
    public String debaterName;
    public String schoolName;
    public double rating; // 2 decimal points
    public double ratingDeviation;
    public int season;
}