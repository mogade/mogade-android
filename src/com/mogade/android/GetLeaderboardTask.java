package com.mogade.android;

import android.os.AsyncTask;
import com.mogade.Driver;
import com.mogade.Guard;
import com.mogade.Response;
import com.mogade.impl.DefaultDriver;
import com.mogade.models.LeaderboardScores;

public class GetLeaderboardTask extends AsyncTask<Void, Void, Response<LeaderboardScores>> {
    protected final Driver driver;
    protected final String leaderboardId;
    protected final int scope;
    protected final int page;
    protected final int records;

    protected ResponseCallback<LeaderboardScores> callback;

    public GetLeaderboardTask( String leaderboardId, int scope, int page, int records) {
        Guard.NotNullOrEmpty(leaderboardId, "leaderboard was empty");

        this.driver = new DefaultDriver();
        this.leaderboardId = leaderboardId;
        this.scope = scope;
        this.page = page;
        this.records = records;
    }

    public GetLeaderboardTask setCallback(ResponseCallback<LeaderboardScores> callback) {
        Guard.NotNull(callback, "callback was null");

        this.callback = callback;
        return this;
    }

    @Override
    protected Response<LeaderboardScores> doInBackground(Void... voids) {
        return driver.getLeaderboard(leaderboardId, scope, page, records);
    }

    @Override
    protected void onPostExecute(Response<LeaderboardScores> response) {
        // invoke callback if provided
        if (callback != null) {
            callback.onComplete(response);
        }
    }
}
