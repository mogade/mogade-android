package com.mogade.android;

import android.os.AsyncTask;
import com.mogade.Driver;
import com.mogade.Guard;
import com.mogade.Response;
import com.mogade.impl.DefaultDriver;

public class GetUserRankTask extends AsyncTask<Void, Void, Response<Integer>> {
    protected final Driver driver;
    protected final String leaderboardId;
    protected final int scope;
    protected final String uniqueIdentifier;
    protected final String username;

    private ResponseCallback<Integer> callback;

    public GetUserRankTask(String gameKey, String secret, String leaderboardId, String username, String uniqueIdentifier, int scope) {
        Guard.NotNullOrEmpty(leaderboardId, "leaderboardId was empty");
        Guard.NotNullOrEmpty(username, "username was empty");
        Guard.NotNullOrEmpty(uniqueIdentifier, "unique identifier was empty");

        this.driver = new DefaultDriver(gameKey, secret);
        this.leaderboardId = leaderboardId;
        this.username = username;
        this.uniqueIdentifier = uniqueIdentifier;
        this.scope = scope;
    }

    public GetUserRankTask setCallback(ResponseCallback<Integer> callback) {
        Guard.NotNull(callback, "callback was null");

        this.callback = callback;
        return this;
    }

    @Override
    protected Response<Integer> doInBackground(Void... voids) {
        return driver.getRank(leaderboardId, username, uniqueIdentifier, scope);
    }

    @Override
    protected void onPostExecute(Response<Integer> response) {
        // invoke callback if specified
        if (callback != null)
            callback.onComplete(response);
    }
}
