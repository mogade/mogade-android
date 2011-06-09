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
     * Retrieves the list of scores for a leaderboard for a specific user.
     *
     * @param leaderboardId    The id for the leaderboard to retrieve
     * @param scope            The scope to limit the scores to
     * @param username         The username to limit the scores by
     * @param uniqueIdentifier The unique identifier of the user to limit the scores by
     * @param records          How many scores should be returned
     * @return The response with the leaderboard scores
     */
    Response<LeaderboardScores> getLeaderboard(String leaderboardId, int scope, String username, String uniqueIdentifier, int records);

    /**
     * Submits a score to the specified leaderboard
     *
     * @param leaderboardId    The leaderboard to submit the score to
     * @param uniqueIdentifier The unique identifier of the user
     * @param score            The score data
     * @return The ranks
     */
    Response<Ranks> submitScore(String leaderboardId, String uniqueIdentifier, Score score);

    /**
     * Retrieves the ranks for a user on a specific leaderboard.
     *
     * @param leaderboardId    The id of the leaderboard
     * @param username         The username for the user
     * @param uniqueIdentifier The unique identifier for the user
     * @param scope            The scope to limit the ranking to
     * @return The ranks for the user
     */
    Response<Integer> getRank(String leaderboardId, String username, String uniqueIdentifier, int scope);

    /**
     * Retrieves the ranks for a user on a specific leaderboard.
     *
     * @param leaderboardId    The id of the leaderboard
     * @param username         The username for the user
     * @param uniqueIdentifier The unique identifier for the user
     * @return The ranks for the user
     */
    Response<Ranks> getRanks(String leaderboardId, String username, String uniqueIdentifier);

    /**
     * Retrieves the ranks for a user on a specific leaderboard.
     *
     * @param leaderboardId    The id of the leaderboard
     * @param username         The username for the user
     * @param uniqueIdentifier The unique identifier for the user
     * @param scopes           The scopes to limit the ranks to
     * @return The ranks for the user
     */
    Response<Ranks> getRanks(String leaderboardId, String username, String uniqueIdentifier, int[] scopes);
}
