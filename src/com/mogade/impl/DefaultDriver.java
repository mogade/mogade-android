package com.mogade.impl;

import com.mogade.*;
import com.mogade.models.*;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

public class DefaultDriver implements Driver {
    private final String gameKey;
    private final String secret;

    public DefaultDriver(String gameKey, String secret) {
        Guard.NotNullOrEmpty(gameKey, "Game key is required.");
        Guard.NotNullOrEmpty(secret, "Secret is required.");

        this.gameKey = gameKey;
        this.secret = secret;
    }

    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public Response<LeaderboardScores> getLeaderboard(String leaderboardId, int scope, int page, int records) {

        DefaultCommunicator communicator = new DefaultCommunicator();
        Map<String, Object> parameters = new HashMap<String, Object>();
        addParameter(parameters, "lid", leaderboardId);
        addParameter(parameters, "lid", leaderboardId);
        addParameter(parameters, "scope", Integer.toString(scope));
        addParameter(parameters, "page", Integer.toString(page));
        addParameter(parameters, "records", Integer.toString(records));

        return communicator.get("scores", parameters, LEADERBOARD_CONVERTER);
    }

    public Response<LeaderboardScores> getLeaderboard(String leaderboardId, int scope, String username, String uniqueIdentifier, int records) {
        DefaultCommunicator communicator = new DefaultCommunicator();
        Map<String, Object> parameters = new HashMap<String, Object>();
        addParameter(parameters, "lid", leaderboardId);
        addParameter(parameters, "scope", Integer.toString(scope));
        addParameter(parameters, "username", username);
        addParameter(parameters, "userKey", uniqueIdentifier);
        addParameter(parameters, "records", Integer.toString(records));

        return communicator.get("scores", parameters, LEADERBOARD_CONVERTER);
    }

    public Response<Ranks> submitScore(String leaderboardId, String uniqueIdentifier, Score score) {
        DefaultCommunicator communicator = new DefaultCommunicator();
        Map<String, Object> parameters = new HashMap<String, Object>();
        addParameter(parameters, "lid", leaderboardId);
        addParameter(parameters, "username", score.getUsername());
        addParameter(parameters, "userkey", uniqueIdentifier);
        addParameter(parameters, "points", Integer.toString(score.getPoints()));
        addParameter(parameters, "data", score.getData());
        addParameter(parameters, "key", gameKey);

        return communicator.post("scores", getSignedParameters(parameters), RANKS_CONVERTER);
    }

    public Response<Integer> getRank(String leaderboardId, String username, String uniqueIdentifier, final int scope) {
        DefaultCommunicator communicator = new DefaultCommunicator();
        Map<String, Object> parameters = new HashMap<String, Object>();
        addParameter(parameters, "lid", leaderboardId);
        addParameter(parameters, "username", username);
        addParameter(parameters, "userkey", uniqueIdentifier);
        addParameter(parameters, "scopes", Integer.toString(scope));

        ResponseConverter<Integer> converter = new ResponseConverter<Integer>() {
            public Integer convert(JSONObject source) throws Exception {
                Ranks rank = RANKS_CONVERTER.convert(source);

                return Ranks.getRankByScope(rank, scope);
            }
        };

        return communicator.get("ranks", parameters, converter);
    }

    public Response<Ranks> getRanks(String leaderboardId, String username, String uniqueIdentifier) {
        int[] scopes = new int[]{LeaderboardScope.DAILY, LeaderboardScope.OVERALL, LeaderboardScope.WEEKLY, LeaderboardScope.YESTERDAY};
        return getRanks(leaderboardId, username, uniqueIdentifier, scopes);
    }

    public Response<Ranks> getRanks(String leaderboardId, String username, String uniqueIdentifier, int[] scopes) {
        DefaultCommunicator communicator = new DefaultCommunicator();
        Map<String, Object> parameters = new HashMap<String, Object>();
        addParameter(parameters, "lid", leaderboardId);
        addParameter(parameters, "username", username);
        addParameter(parameters, "userkey", uniqueIdentifier);
        for (int scope : scopes)
            addArrayParameter(parameters, "scopes[]", Integer.toString(scope));

        return communicator.get("ranks", parameters, RANKS_CONVERTER);
    }

    public Response<Achievement> achievementEarned(String achievementId, String username, String uniqueIdentifier) {
        DefaultCommunicator communicator = new DefaultCommunicator();
        Map<String, Object> parameters = new HashMap<String, Object>();
        addParameter(parameters, "aid", achievementId);
        addParameter(parameters, "username", username);
        addParameter(parameters, "userKey", uniqueIdentifier);
        addParameter(parameters, "key", gameKey);

        return communicator.post("achievements", getSignedParameters(parameters), ACHIEVEMENT_CONVERTER);
    }

    public Response<List<Achievement>> getEarnedAchievements(String username, String uniqueIdentifier) {
        DefaultCommunicator communicator = new DefaultCommunicator();
        Map<String, Object> parameters = new HashMap<String, Object>();
        addParameter(parameters, "username", username);
        addParameter(parameters, "userKey", uniqueIdentifier);
        addParameter(parameters, "key", gameKey);

        return communicator.get("achievements", parameters, ACHIEVEMENT_LIST_CONVERTER);
    }

    public void logApplicationStart(String uniqueIdentifier) {
        DefaultCommunicator communicator = new DefaultCommunicator();
        Map<String, Object> parameters = new HashMap<String, Object>();
        addParameter(parameters, "userKey", uniqueIdentifier);
        addParameter(parameters, "key", gameKey);

        communicator.post("stats", getSignedParameters(parameters), new ResponseConverter<Void>() {
            public Void convert(JSONObject source) throws Exception {
                return null;
            }
        });
    }

    public void logError(String subject, String details) {
        DefaultCommunicator communicator = new DefaultCommunicator();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("subject", subject);
        parameters.put("details", details);

        communicator.post("errors", parameters, new ResponseConverter<Void>() {
            public Void convert(JSONObject source) throws Exception {
                return null;
            }
        });
    }

    private Map<String, Object> getSignedParameters(Map<String, Object> parameters) {
        SortedMap<String, Object> signed = new TreeMap<String, Object>(parameters);

        Map<String, Object> result = new HashMap<String, Object>(parameters);
        addParameter(result, "sig", SignatureGenerator.generate(signed, secret));

        return result;
    }

    private static void addArrayParameter(Map<String, Object> parameters, String key, String value) {
        if (!parameters.containsKey(key)) {
            parameters.put(key, new ArrayList<String>());
        }

        List<String> array = (List<String>) parameters.get(key);
        array.add(value);
    }

    private static void addParameter(Map<String, Object> parameters, String key, String value) {
        parameters.put(key, value);
    }

    private static final ArrayResponseConverter<List<Achievement>> ACHIEVEMENT_LIST_CONVERTER = new ArrayResponseConverter<List<Achievement>>() {
        public List<Achievement> convert(JSONArray source) throws Exception {
            int length = source.length();
            List<Achievement> results = new ArrayList<Achievement>(length);
            for (int i = 0; i < length; i++) {
                Achievement achievement = new Achievement();
                achievement.setId(source.getString(i));

                results.add(achievement);
            }

            return results;
        }
    };

    private static final ResponseConverter<Ranks> RANKS_CONVERTER = new ResponseConverter<Ranks>() {
        public Ranks convert(JSONObject source) throws Exception {
            Ranks result = new Ranks();
            result.daily = source.getInt("daily");
            result.overall = source.getInt("overall");
            result.weekly = source.getInt("weekly");
            result.yesterday = source.getInt("yesterday");

            return result;
        }
    };

    private static final ResponseConverter<LeaderboardScores> LEADERBOARD_CONVERTER = new ResponseConverter<LeaderboardScores>() {
        public LeaderboardScores convert(JSONObject source) throws Exception {
            LeaderboardScores scores = new LeaderboardScores();
            scores.setPage(source.getInt("page"));

            JSONArray jsonScores = source.optJSONArray("scores");
            int scoreCount = jsonScores.length();
            List<Score> scoreList = new ArrayList<Score>(scoreCount);
            for (int index = 0; index < scoreCount; index++) {
                JSONObject jsonScore = jsonScores.getJSONObject(index);
                Score score = new Score();
                score.setUsername(jsonScore.getString("username"));
                score.setPoints(jsonScore.getInt("points"));
                score.setDated(DATE_FORMAT.parse(jsonScore.getString("dated")));
                score.setData(jsonScore.getString("data"));

                scoreList.add(score);
            }

            scores.setScores(scoreList);
            return scores;
        }
    };

    private static final ResponseConverter<Achievement> ACHIEVEMENT_CONVERTER = new ResponseConverter<Achievement>() {
        public Achievement convert(JSONObject source) throws Exception {
            Achievement achievement = new Achievement();
            achievement.setId(source.getString("id"));
            achievement.setPoints(source.getInt("points"));

            return achievement;
        }
    };
}
