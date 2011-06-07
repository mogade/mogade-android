package com.mogade;

import com.mogade.models.LeaderboardScores;
import com.mogade.models.Ranks;
import com.mogade.models.Score;

public interface Driver {
    /**
     * Retrieves the list of scores for a leaderboard
     *
     * @param leaderboardId The id for the leaderboard
     * @param scope         The scope to limit the scores to
     * @param page          The page to retrieve scores for
     * @param records       How many scores should be returned
     * @return The response with the leaderboard scores
     */
    Response<LeaderboardScores> getLeaderboard(String leaderboardId, int scope, int page, int records);

    /**
     * Submits a score to the specified leaderboard
     *
     * @param leaderboardId    The leaderboard to submit the score to
     * @param uniqueIdentifier The unique identifier for the score
     * @param score            The score data
     * @return The ranks
     */
    Response<Ranks> submitScore(String leaderboardId, String uniqueIdentifier, Score score);
}
