package com.mogade.impl;

import com.mogade.Driver;
import com.mogade.Guard;
import com.mogade.Response;
import com.mogade.ResponseConverter;
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
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("lid", leaderboardId);
        parameters.put("scope", Integer.toString(scope));
        parameters.put("page", Integer.toString(page));
        parameters.put("records", Integer.toString(records));

        return communicator.get("scores", parameters, LEADERBOARD_CONVERTER);
    }

    public Response<LeaderboardScores> getLeaderboard(String leaderboardId, int scope, String username, String uniqueIdentifier, int records) {
        DefaultCommunicator communicator = new DefaultCommunicator();
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("lid", leaderboardId);
        parameters.put("scope", Integer.toString(scope));
        parameters.put("username", username);
        parameters.put("userKey", uniqueIdentifier);
        parameters.put("records", Integer.toString(records));

        return communicator.get("scores", parameters, LEADERBOARD_CONVERTER);
    }

    public Response<Ranks> submitScore(String leaderboardId, String uniqueIdentifier, Score score) {
        DefaultCommunicator communicator = new DefaultCommunicator();
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("lid", leaderboardId);
        parameters.put("username", score.getUsername());
        parameters.put("userkey", uniqueIdentifier);
        parameters.put("points", Integer.toString(score.getPoints()));
        parameters.put("data", score.getData());
        parameters.put("key", gameKey);

        return communicator.post("scores", getSignedParameters(parameters), RANKS_CONVERTER);
    }

    public Response<Integer> getRank(String leaderboardId, String username, String uniqueIdentifier, final int scope) {
        DefaultCommunicator communicator = new DefaultCommunicator();
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("lid", leaderboardId);
        parameters.put("username", username);
        parameters.put("userkey", uniqueIdentifier);
        parameters.put("scopes", Integer.toString(scope));

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
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("lid", leaderboardId);
        parameters.put("username", username);
        parameters.put("userkey", uniqueIdentifier);
        parameters.put("scopes", encodeArray(scopes));

        return communicator.get("ranks", parameters, RANKS_CONVERTER);
    }

    public Response<Achievement> achievementEarned(String achievementId, String username, String uniqueIdentifier) {
        DefaultCommunicator communicator = new DefaultCommunicator();
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("aid", achievementId);
        parameters.put("username", username);
        parameters.put("userKey", uniqueIdentifier);
        parameters.put("key", gameKey);

        return communicator.post("achievements", getSignedParameters(parameters), ACHIEVEMENT_CONVERTER);
    }

    private Map<String, String> getSignedParameters(Map<String, String> parameters) {
        SortedMap<String, String> signed = new TreeMap<String, String>(parameters);

        Map<String, String> result = new HashMap<String, String>(parameters);
        result.put("sig", SignatureGenerator.generate(signed, secret));

        return result;
    }

    private static String encodeArray(int[] items) {
        return "????";
    }

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
