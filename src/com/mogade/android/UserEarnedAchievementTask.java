package com.mogade.android;

import android.os.AsyncTask;
import com.mogade.Driver;
import com.mogade.Guard;
import com.mogade.Response;
import com.mogade.impl.DefaultDriver;
import com.mogade.models.Achievement;

public class UserEarnedAchievementTask extends AsyncTask<Void, Void, Response<Achievement>> {
    protected final Driver driver;
    protected final String achievementId;
    protected final String username;
    protected final String uniqueIdentifier;
    private ResponseCallback<Achievement> callback;

    public UserEarnedAchievementTask(String gameKey, String secret, String achievementId, String username, String uniqueIdentifier) {
        Guard.NotNullOrEmpty(achievementId, "achievementId was empty");
        Guard.NotNullOrEmpty(username, "username was empty");
        Guard.NotNullOrEmpty(uniqueIdentifier, "unique identifier was empty");

        this.driver = new DefaultDriver(gameKey, secret);
        this.achievementId = achievementId;
        this.username = username;
        this.uniqueIdentifier = uniqueIdentifier;
    }

    public UserEarnedAchievementTask setCallback(ResponseCallback<Achievement> callback) {
        Guard.NotNull(callback, "callback was null");

        this.callback = callback;
        return this;
    }

    @Override
    protected Response<Achievement> doInBackground(Void... voids) {
        return driver.achievementEarned(achievementId, username, uniqueIdentifier);
    }

    @Override
    protected void onPostExecute(Response<Achievement> response) {
        // invoke callback if specifeid
        if (callback != null)
            callback.onComplete(response);
    }
}
