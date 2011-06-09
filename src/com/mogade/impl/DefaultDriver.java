package com.mogade.impl;

import com.mogade.Driver;
import com.mogade.Guard;
import com.mogade.Response;
import com.mogade.ResponseConverter;
import com.mogade.models.LeaderboardScores;
import com.mogade.models.Ranks;
import com.mogade.models.Score;
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

    private Map<String, String> getSignedParameters(Map<String, String> parameters) {
        SortedMap<String, String> signed = new TreeMap<String, String>(parameters);

        Map<String, String> result = new HashMap<String, String>(parameters);
        result.put("sig", SignatureGenerator.generate(signed, secret));

        return result;
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
}
