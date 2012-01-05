package com.mogade;

import com.mogade.models.*;

import java.util.List;

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
     * Retrieves the list of scores for a leaderboard for a specific user.
     *
     * @param leaderboardId    The id for the leaderboard to retrieve
     * @param scope            The scope to limit the scores to
     * @param username         The username to limit the scores by
     * @param uniqueIdentifier The unique identifier of the user to limit the scores by
     * @return The response with the leaderboard scores
     */
    Response<Score> getLeaderboard(String leaderboardId, int scope, String username, String uniqueIdentifier);

     /**
     * Retrieves the leaderboard page with a specific number of records and the player's stats.
     * @param leaderboardId     The id for the leaderboard to retrieve.
     * @param scope             The scope to limit the scores to.
     * @param username          The name of the user.
     * @param uniqueIdentifier  The unique identifier for the user.
     * @param page              The page to get.
     * @param records           The number of records to retrieve.
     * @return                  A leaderboard object with the scores.
     */
    Response<LeaderboardScoresWithPlayerStats> getLeaderboardWithPlayerStats(String leaderboardId, int scope, String username, String uniqueIdentifier, int page, int records);

    /**
     * Gets the number of scores in a leaderboard (up to 25,000)
     * @param leaderboardId The id of the leaderboard to get scores from.
     * @param scope         The scope to limit the scores to.
     * @return              The number of scores in the leaderboard.
     */
    Response<Integer> getLeaderboardCount(String leaderboardId, int scope);

    /**
     * Gets the scores of players which are immediately ahead of the specified player in the leaderboard.
     * @param leaderboardId     The id of the leaderboard to retrieve scores from.
     * @param scope             The scope to limit scores to.
     * @param username          The name of the user.
     * @param uniqueIdentifier  The unique identifier for the user.
     * @return                  The scores of the closest 3 players.
     */
    Response<List<Score>> getRivals(String leaderboardId, int scope, String username, String uniqueIdentifier);

    /**
     * Submits a score to the specified leaderboard
     *
     * @param leaderboardId    The leaderboard to submit the score to
     * @param uniqueIdentifier The unique identifier of the user
     * @param score            The score data
     * @return The ranks
     */
    Response<SavedScore> submitScore(String leaderboardId, String uniqueIdentifier, Score score);

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
     * Retrieves the rank for a score across an individual scope.
     * @param leaderboardId The id of the leaderboard to filter scores to.
     * @param score       The score to compare.
     * @param scope       The scope to limit scores to.
     * @return            The rank for the score.
     */
    Response<Integer> getRank(String leaderboardId, int score, int scope);

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

    /**
     * Gets the rank for a score across all scopes.
     * @param leaderboardId The id of the leaderboard to limit scores to.
     * @param score         The score to compare.
     * @return              The ranks for that score.
     */
    Response<Ranks> getRanks(String leaderboardId, int score);

    /**
     * Gets the rank for a score across the specified scopes.
     * @param leaderboardId The id of the leaderboard to limit scores to.
     * @param score         The score to compare.
     * @param scopes        The scopes to limit scores to.
     * @return              The ranks for the score.
     */
    Response<Ranks> getRanks(String leaderboardId, int score, int[] scopes);

    /**
     * Gets the achievements that have been earned by a specific user
     *
     * @param username         The username for the user to retrieve the achievements for
     * @param uniqueIdentifier The id of the user
     * @return The list of achievements that have been earned by the user.
     */
    Response<List<Achievement>> getEarnedAchievements(String username, String uniqueIdentifier);

    /**
     * Gets all of the achievements
     * @return The achievements in the system.
     */
    Response<List<Achievement>> getAchievements();

    /**
     * Records that a user earned the specified achievement
     *
     * @param achievementId    The identifier of the achievement that was earned.
     * @param username         The username of the user who earned the achievement
     * @param uniqueIdentifier The id of the user who earned the achievement
     * @return The achievement
     */
    Response<Achievement> achievementEarned(String achievementId, String username, String uniqueIdentifier);

    /**
     * Logs an application start
     * @param uniqueIdentifier The unique identifier for a user.
     * @return                 Nothing
     */
    Response<Void> logApplicationStart(String uniqueIdentifier);

    /**
     * Logs a hit for today to a custom stat counter.
     * @param index The statistic to count.
     * @return      Nothing.
     */
    Response<Void> logCustomStat(int index);

    /**
     * Logs an error.
     * @param subject The subject of the error (brief description).
     * @param details The error's details.
     * @return        Nothing.
     */
    Response<Void> logError(String subject, String details);

    /**
     * Gets the assets for the game
     * @return The list of assets
     */
    Response<List<Asset>> getAssets();
}
