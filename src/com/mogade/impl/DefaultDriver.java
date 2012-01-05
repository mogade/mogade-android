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

    public DefaultDriver() {
        this.gameKey = null;
        this.secret = null;
    }

    public DefaultDriver(String gameKey) {
        Guard.NotNullOrEmpty(gameKey, "Game key is required.");

        this.gameKey = gameKey;
        this.secret = null;
    }

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

    public Response<Score> getLeaderboard(String leaderboardId, int scope, String username, String uniqueIdentifier) {
        DefaultCommunicator communicator = new DefaultCommunicator();
        Map<String, Object> parameters = new HashMap<String, Object>();
        addParameter(parameters, "lid", leaderboardId);
        addParameter(parameters, "scope", Integer.toString(scope));
        addParameter(parameters, "username", username);
        addParameter(parameters, "userkey", uniqueIdentifier);
        addParameter(parameters, "records", "1");

        return communicator.get("scores", parameters, SCORE_CONVERTER);
    }

    public Response<LeaderboardScoresWithPlayerStats> getLeaderboardWithPlayerStats(String leaderboardId, int scope, String username, String uniqueIdentifier, int page, int records) {
        DefaultCommunicator communicator = new DefaultCommunicator();
        Map<String, Object> parameters = new HashMap<String, Object>();
        addParameter(parameters, "lid", leaderboardId);
        addParameter(parameters, "with_player", "true");
        addParameter(parameters, "scope", Integer.toString(scope));
        addParameter(parameters, "username", username);
        addParameter(parameters, "userKey", uniqueIdentifier);
        addParameter(parameters, "records", Integer.toString(records));
        addParameter(parameters, "page", Integer.toString(page));

        return communicator.get("scores", parameters, LEADERBOARD_SCORES_WITH_PLAYER_STATS_CONVERTER);
    }
    
    public Response<Integer> getLeaderboardCount(String leaderboardId, int scope) {
        DefaultCommunicator communicator = new DefaultCommunicator();
        Map<String, Object> parameters = new HashMap<String, Object>();
        addParameter(parameters, "lid", leaderboardId);
        addParameter(parameters, "scope", Integer.toString(scope));

        return communicator.get("scores/count", parameters, new RawResponseConverter<Integer>() {
            public Integer convert(String body) throws Exception {
                return Integer.parseInt(body);
            }
        });
    }

    public Response<List<Score>> getRivals(String leaderboardId, int scope, String username, String uniqueIdentifier) {
        DefaultCommunicator communicator = new DefaultCommunicator();
        Map<String, Object> parameters = new HashMap<String, Object>();
        addParameter(parameters, "lid", leaderboardId);
        addParameter(parameters, "scope", Integer.toString(scope));
        addParameter(parameters, "username", username);
        addParameter(parameters, "userKey", uniqueIdentifier);

        return communicator.get("scores/rivals", parameters, SCORE_LIST_CONVERTER);
    }

    public Response<SavedScore> submitScore(String leaderboardId, String uniqueIdentifier, Score score) {
        DefaultCommunicator communicator = new DefaultCommunicator();
        Map<String, Object> parameters = new HashMap<String, Object>();
        addParameter(parameters, "lid", leaderboardId);
        addParameter(parameters, "username", score.getUsername());
        addParameter(parameters, "userkey", uniqueIdentifier);
        addParameter(parameters, "points", Integer.toString(score.getPoints()));
        addParameter(parameters, "data", score.getData());
        addParameter(parameters, "key", gameKey);

        return communicator.post("scores", getSignedParameters(parameters), SAVED_SCORE_CONVERTER);
    }

    public Response<Integer> getRank(String leaderboardId, String username, String uniqueIdentifier, final int scope) {
        DefaultCommunicator communicator = new DefaultCommunicator();
        Map<String, Object> parameters = new HashMap<String, Object>();
        addParameter(parameters, "lid", leaderboardId);
        addParameter(parameters, "username", username);
        addParameter(parameters, "userkey", uniqueIdentifier);
        addParameter(parameters, "scopes", Integer.toString(scope));

        RawResponseConverter<Integer> converter = new RawResponseConverter<Integer>() {
            public Integer convert(String body) throws Exception {
                return Integer.parseInt(body);
            }
        };

        return communicator.get("ranks", parameters, converter);
    }
    
    public Response<Integer> getRank(String leaderboardId, int score, int scope) {

        DefaultCommunicator communicator = new DefaultCommunicator();
        Map<String, Object> parameters = new HashMap<String, Object>();
        addParameter(parameters, "lid", leaderboardId);
        addParameter(parameters, "score", Integer.toString(score));
        addParameter(parameters, "scopes", Integer.toString(scope));

        RawResponseConverter<Integer> converter = new RawResponseConverter<Integer>() {
            public Integer convert(String body) throws Exception {
                return Integer.parseInt(body);
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

    public Response<Ranks> getRanks(String leaderboardId, int score) {
        int[] scopes = new int[]{LeaderboardScope.DAILY, LeaderboardScope.OVERALL, LeaderboardScope.WEEKLY, LeaderboardScope.YESTERDAY};
        
        return getRanks(leaderboardId, score, scopes);
    }

    public Response<Ranks> getRanks(String leaderboardId, int score, int[] scopes) {
        DefaultCommunicator communicator = new DefaultCommunicator();
        Map<String, Object> parameters = new HashMap<String, Object>();
        addParameter(parameters, "lid", leaderboardId);
        addParameter(parameters, "score", Integer.toString(score));
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

    public Response<List<Achievement>> getAchievements() {
        DefaultCommunicator communicator = new DefaultCommunicator();
        Map<String, Object> parameters = new HashMap<String, Object>();
        addParameter(parameters, "key", gameKey);

        return communicator.get("achievements", parameters, ACHIEVEMENT_LIST_CONVERTER);
    }

    public Response<Void> logApplicationStart(String uniqueIdentifier) {
        DefaultCommunicator communicator = new DefaultCommunicator();
        Map<String, Object> parameters = new HashMap<String, Object>();
        addParameter(parameters, "userKey", uniqueIdentifier);
        addParameter(parameters, "key", gameKey);

        return communicator.post("stats", getSignedParameters(parameters), VOID_CONVERTER);
    }

    public Response<Void> logError(String subject, String details) {
        DefaultCommunicator communicator = new DefaultCommunicator();
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("subject", subject);
        parameters.put("details", details);

        return communicator.post("errors", parameters, VOID_CONVERTER);
    }
    
    public Response<Void> logCustomStat(int index) {
        DefaultCommunicator communicator = new DefaultCommunicator();
        Map<String,Object> parameters = new HashMap<String, Object>(1);
        addParameter(parameters, "custom", Integer.toString(index));
        
        return communicator.post("stats", parameters, VOID_CONVERTER);
    }

    public Response<List<Asset>> getAssets() {
        DefaultCommunicator communicator = new DefaultCommunicator();
        Map<String,Object> parameters = new HashMap<String, Object>();
        addParameter(parameters, "key", gameKey);

        return communicator.get("assets", parameters, ASSET_LIST_CONVERTER);
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
    
    private static final ResponseConverter<Void> VOID_CONVERTER = new ResponseConverter<Void>() {
        public Void convert(JSONObject source) throws Exception {
            return null;
        }
    };

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
    
    private static final ResponseConverter<SavedScore> SAVED_SCORE_CONVERTER = new ResponseConverter<SavedScore>() {
        public SavedScore convert(JSONObject source) throws Exception {
            SavedScore result = new SavedScore();
            result.Ranks = RANKS_CONVERTER.convert(source.getJSONObject("ranks"));
            result.Highs = HIGHS_CONVERTER.convert(source.getJSONObject("highs"));
            return result;
        }
    };
    
    private static final ResponseConverter<Highs> HIGHS_CONVERTER = new ResponseConverter<Highs>() {
        public Highs convert(JSONObject source) throws Exception {
            Highs result = new Highs();
            result.Daily = source.getBoolean("1");
            result.Weekly = source.getBoolean("2");
            result.Overall = source.getBoolean("3");

            return result;
        }
    };

    private static final ResponseConverter<Ranks> RANKS_CONVERTER = new ResponseConverter<Ranks>() {
        public Ranks convert(JSONObject source) throws Exception {
            Ranks result = new Ranks();
            result.daily = source.getInt("1");
            result.overall = source.getInt("3");
            result.weekly = source.getInt("2");
            result.yesterday = source.getInt("4");

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

    private static final ArrayResponseConverter<List<Asset>> ASSET_LIST_CONVERTER = new ArrayResponseConverter<List<Asset>>() {
        public List<Asset> convert(JSONArray source) throws Exception {
            int length = source.length();
            List<Asset> results = new ArrayList<Asset>(length);
            for (int i = 0; i < length; i++) {
                JSONObject jsonAsset = source.getJSONObject(i);
                Asset asset = new Asset();
                asset.setName(jsonAsset.getString("name"));
                asset.setFile(jsonAsset.getString("file"));
                asset.setMeta(jsonAsset.getString("meta"));
                asset.setType(jsonAsset.getInt("type"));
                asset.setDated(DATE_FORMAT.parse(jsonAsset.getString("dated")));

                results.add(asset);
            }

            return results;
        }
    };
    
    private static final ResponseConverter<Score> SCORE_CONVERTER = new ResponseConverter<Score>() {
        public Score convert(JSONObject source) throws Exception {
            Score result = new Score();
            result.setUsername(source.getString("username"));
            if (source.has("data"))
                result.setData(source.getString("data"));
            result.setPoints(source.getInt("points"));
            if (source.has("dated"))
                result.setDated(DATE_FORMAT.parse(source.getString("dated")));
            return result;
        }
    };
    
    private static final ArrayResponseConverter<List<Score>> SCORE_LIST_CONVERTER = new ArrayResponseConverter<List<Score>>() {
        public List<Score> convert(JSONArray source) throws Exception {
            int length = source.length();
            List<Score> results = new ArrayList<Score>(length);
            for (int i = 0; i < length; i++) {
                JSONObject jsonScore = source.getJSONObject(i);
                results.add(SCORE_CONVERTER.convert(jsonScore));
            }

            return results;
        }
    };
    
    private static final ResponseConverter<LeaderboardScoresWithPlayerStats> LEADERBOARD_SCORES_WITH_PLAYER_STATS_CONVERTER = new ResponseConverter<LeaderboardScoresWithPlayerStats>() {
        public LeaderboardScoresWithPlayerStats convert(JSONObject source) throws Exception {
            LeaderboardScoresWithPlayerStats result = new LeaderboardScoresWithPlayerStats();
            result.setRank(source.getInt("rank"));
            result.setPlayer(SCORE_CONVERTER.convert(source.getJSONObject("player")));
            result.setScores(LEADERBOARD_CONVERTER.convert(source.getJSONObject("scores")));
            
            return result;
        }
    };
}
