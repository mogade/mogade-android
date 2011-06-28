package com.mogade.android;

import android.os.AsyncTask;
import com.mogade.Driver;
import com.mogade.Guard;
import com.mogade.Response;
import com.mogade.impl.DefaultDriver;
import com.mogade.models.LeaderboardScores;

public class GetUserLeaderboardTask extends AsyncTask<Void, Void, Response<LeaderboardScores>> {
    protected final Driver driver;
    protected final String leaderboardId;
    protected final int scope;
    protected final int records;
    protected final String username;
    protected final String uniqueIdentifier;

    protected ResponseCallback<LeaderboardScores> callback;

    public GetUserLeaderboardTask(String leaderboardId, int scope, String username, String uniqueIdentifier, int records) {
        Guard.NotNullOrEmpty(leaderboardId, "leaderboard was empty");
        Guard.NotNullOrEmpty(username, "username was empty");
        Guard.NotNullOrEmpty(uniqueIdentifier, "uniqueIdentifier was empty");

        this.driver = new DefaultDriver();
        this.leaderboardId = leaderboardId;
        this.scope = scope;
        this.username = username;
        this.uniqueIdentifier = uniqueIdentifier;
        this.records = records;
    }

    public GetUserLeaderboardTask setCallback(ResponseCallback<LeaderboardScores> callback) {
        Guard.NotNull(callback, "callback was null");

        this.callback = callback;
        return this;
    }

    @Override
    protected Response<LeaderboardScores> doInBackground(Void... voids) {
        return driver.getLeaderboard(leaderboardId, scope, username, uniqueIdentifier, records);
    }

    @Override
    protected void onPostExecute(Response<LeaderboardScores> response) {
        // invoke callback if specified
        if (callback != null)
            callback.onComplete(response);
    }
}
