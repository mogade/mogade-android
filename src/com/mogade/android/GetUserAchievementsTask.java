package com.mogade.android;

import android.os.AsyncTask;
import com.mogade.Driver;
import com.mogade.Guard;
import com.mogade.Response;
import com.mogade.impl.DefaultDriver;
import com.mogade.models.Achievement;

import java.util.List;

public class GetUserAchievementsTask extends AsyncTask<Void, Void, Response<List<Achievement>>> {
    protected final Driver driver;
    protected final String gameKey;
    protected final String username;
    protected final String uniqueIdentifier;
    private ResponseCallback<List<Achievement>> callback;

    public GetUserAchievementsTask(String gameKey, String username, String uniqueIdentifier) {
        Guard.NotNullOrEmpty(username, "username was empty");
        Guard.NotNullOrEmpty(uniqueIdentifier, "unique identifier was empty");

        this.driver = new DefaultDriver(gameKey);
        this.gameKey = gameKey;
        this.username = username;
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public GetUserAchievementsTask setCallback(ResponseCallback<List<Achievement>> callback) {
        Guard.NotNull(callback, "callback was null");

        this.callback = callback;
        return this;
    }

    @Override
    protected Response<List<Achievement>> doInBackground(Void... voids) {
        return driver.getEarnedAchievements(username, uniqueIdentifier);
    }

    @Override
    protected void onPostExecute(Response<List<Achievement>> response) {
        if (callback != null)
            callback.onComplete(response);
    }
}
