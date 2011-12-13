package com.mogade.models;

public class LeaderboardScoresWithPlayerStats {
    private LeaderboardScores scores;
    private int rank;
    private Score player;

    public LeaderboardScores getScores() {
        return scores;
    }

    public void setScores(LeaderboardScores scores) {
        this.scores = scores;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public Score getPlayer() {
        return player;
    }

    public void setPlayer(Score player) {
        this.player = player;
    }
}
