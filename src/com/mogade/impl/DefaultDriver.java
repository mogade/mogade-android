package com.mogade.impl;

import com.mogade.Driver;
import com.mogade.Response;
import com.mogade.ResponseConverter;
import com.mogade.models.LeaderboardScores;
import com.mogade.models.Score;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultDriver implements Driver {
    private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
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

    public Response<LeaderboardScores> GetLeaderboard(String leaderboardId, int scope, int page, int records) {

        DefaultCommunicator communicator = new DefaultCommunicator();
        Map<String, String> parameters = new HashMap<String, String>();
        parameters.put("lid", leaderboardId);
        parameters.put("scope", Integer.toString(scope));
        parameters.put("page", Integer.toString(page));
        parameters.put("records", Integer.toString(records));

        return communicator.Get("scores", parameters, LEADERBOARD_CONVERTER);
    }
}
