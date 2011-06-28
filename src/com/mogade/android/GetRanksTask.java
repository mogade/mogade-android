package com.mogade.android;

import android.os.AsyncTask;
import com.mogade.Driver;
import com.mogade.Guard;
import com.mogade.Response;
import com.mogade.impl.DefaultDriver;
import com.mogade.models.LeaderboardScope;
import com.mogade.models.Ranks;

public class GetRanksTask extends AsyncTask<Void, Void, Response<Ranks>> {
    protected final Driver driver;
    protected final String leaderboardId;
    protected final int[] scopes;
    protected final String uniqueIdentifier;
    protected final String username;

    private ResponseCallback<Ranks> callback;

    public GetRanksTask(String gameKey, String secret, String leaderboardId, String username, String uniqueIdentifier) {
        this(gameKey, secret, leaderboardId, username, uniqueIdentifier, new int[]{LeaderboardScope.DAILY, LeaderboardScope.OVERALL, LeaderboardScope.WEEKLY, LeaderboardScope.YESTERDAY});
    }

    public GetRanksTask(String gameKey, String secret, String leaderboardId, String username, String uniqueIdentifier, int[] scopes) {
        Guard.NotNullOrEmpty(leaderboardId, "leaderboardId was empty");
        Guard.NotNullOrEmpty(username, "username was empty");
        Guard.NotNullOrEmpty(uniqueIdentifier, "unique identifier was empty");

        this.driver = new DefaultDriver(gameKey, secret);
        this.leaderboardId = leaderboardId;
        this.username = username;
        this.uniqueIdentifier = uniqueIdentifier;
        this.scopes = scopes;
    }

    public GetRanksTask setCallback(ResponseCallback<Ranks> callback) {
        Guard.NotNull(callback, "callback was null");

        this.callback = callback;
        return this;
    }

    @Override
    protected Response<Ranks> doInBackground(Void... voids) {
        return driver.getRanks(leaderboardId, username, uniqueIdentifier, scopes);
    }

    @Override
    protected void onPostExecute(Response<Ranks> response) {
        // invoke callback if specified
        if (callback != null)
            callback.onComplete(response);
    }
}
