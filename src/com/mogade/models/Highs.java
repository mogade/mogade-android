package com.mogade.models;

public class Highs {
    public boolean Daily;
    public boolean Weekly;
    public boolean Overall;
    
    public boolean getByScope(int scope) {

        switch (scope) {
            case LeaderboardScope.DAILY:
                return Daily;
            case LeaderboardScope.OVERALL:
                return Overall;
            case LeaderboardScope.WEEKLY:
                return Weekly;
        }

        return false;
    }
}
