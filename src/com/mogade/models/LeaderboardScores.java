package com.mogade.models;

import java.util.List;

public class LeaderboardScores {
    private int page;
    private List<Score> scores;

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public List<Score> getScores() {
        return scores;
    }

    public void setScores(List<Score> scores) {
        this.scores = scores;
    }
}
