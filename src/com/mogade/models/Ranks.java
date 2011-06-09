package com.mogade.models;

public class Ranks {
    public int daily = 0;
    public int weekly = 0;
    public int overall = 0;
    public int yesterday = 0;

    public static int getRankByScope(Ranks rank, int scope) {
        switch (scope) {
            case LeaderboardScope.DAILY:
                return rank.daily;
            case LeaderboardScope.OVERALL:
                return rank.overall;
            case LeaderboardScope.WEEKLY:
                return rank.weekly;
            case LeaderboardScope.YESTERDAY:
                return rank.yesterday;
            default:
                throw new IllegalArgumentException("Invalid scope.");
        }
    }
}
