package com.mogade.android.Scores;

import android.app.Application;
import com.mogade.Guard;

import android.os.AsyncTask;
import com.mogade.MogadeConfiguration;
import com.mogade.models.LeaderboardScope;
import com.mogade.models.Score;
import com.mogade.util.RequestBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.atomic.AtomicReferenceArray;

public class GetScoresTask extends AsyncTask<Void,Void,Void> {
    private static final DateFormat DATED_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private static final String ENDPOINT = "scores";

	private String leaderboardId;
	private int scope = LeaderboardScope.DAILY;
	private int page = 1;
	private int records = 10;

	public GetScoresTask(String leaderboardId){
		Guard.NotNullOrEmpty(leaderboardId, "A valid leaderboard id is required.");
		
		this.leaderboardId = leaderboardId;
	}

	@Override
	protected Void doInBackground(Void... params) {

        try
        {
            DefaultHttpClient client = new DefaultHttpClient();

            Map<String,String> parameters = new HashMap<String, String>(4);
            parameters.put("lid", leaderboardId);
            parameters.put("page", Integer.toString(page));
            parameters.put("scope", Integer.toString(scope));
            parameters.put("records", Integer.toString(records));


            HttpGet get = RequestBuilder.get(ENDPOINT, parameters);
            ResponseHandler<String> handler = new BasicResponseHandler();

            String responseText = client.execute(get, handler);
            JSONObject response = new JSONObject(responseText);

            if (response.has("error")) {
                // TODO: handle error
            } else {
                // parse the result
            }
        }
        catch (Exception ex)
        {
            // TODO: flag error
        }

        return null;
	}

    public void setScope(int scope) {
		this.scope = scope;
	}

	public int getScope() {
		return scope;
	}

	public GetScoresTask setPage(int value) {
		Guard.GreaterThanZero(value, "Invalid page. Page must be greater than zero.");
		this.page = value;
		
		return this;
	}

	public int getPage() {
		return page;
	}

	public GetScoresTask setRecords(int value) {
		Guard.GreaterThanZero(value, "Invalid record count. Records parameter must be greater than zero.");
		this.records = value;
		
		return this;
	}

	public int getRecords() {
		return records;
	}

    public static class ScoresResult {
        private final int page;
        private final List<Score> scores;

        protected ScoresResult(int page, List<Score> scores) {
            this.page = page;
            this.scores = scores;
        }

        static ScoresResult parse(JSONObject json) throws JSONException, ParseException {
            int page = json.getInt("page");
            JSONArray jsonScores = json.getJSONArray("scores");
            int count = jsonScores.length();

            List<Score> scores = new ArrayList<Score>(count);

            for(int index = 0; index < count; index++) {
                JSONObject jsonScore = jsonScores.getJSONObject(index);

                Score score = new Score();
                score.setUsername(jsonScore.getString("username"));
                score.setPoints(jsonScore.getInt("points"));
                score.setData(jsonScore.getString("data"));
                score.setDated(DATED_FORMAT.parse(jsonScore.getString("dated")));

                scores.add(score);
            }

            return new ScoresResult(page, scores);
        }
    }
}
