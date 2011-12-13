package com.mogade.android;

import android.os.AsyncTask;
import com.mogade.Driver;
import com.mogade.Guard;
import com.mogade.Response;
import com.mogade.impl.DefaultDriver;
import com.mogade.models.Ranks;
import com.mogade.models.SavedScore;
import com.mogade.models.Score;

public class SubmitScoreTask extends AsyncTask<Void, Void, Response<SavedScore>> {
    protected final Driver driver;
    protected final String leaderboardId;
    protected final String uniqueIdentifier;
    protected final Score score;

    protected ResponseCallback<SavedScore> callback;

    public SubmitScoreTask(String gameKey, String secret, String leaderboardId, String uniqueIdentifier, Score score) {
        Guard.NotNullOrEmpty(leaderboardId, "leaderboard was empty");
        Guard.NotNullOrEmpty(uniqueIdentifier, "uniqueIdentifier was empty");
        Guard.NotNull(score, "score was null");

        this.driver = new DefaultDriver(gameKey, secret);
        this.leaderboardId = leaderboardId;
        this.uniqueIdentifier = uniqueIdentifier;
        this.score = score;
    }

    public SubmitScoreTask setCallback(ResponseCallback<SavedScore> callback) {
        Guard.NotNull(callback, "callback was null");

        this.callback = callback;
        return this;
    }

    @Override
    protected Response<SavedScore> doInBackground(Void... voids) {
        return driver.submitScore(leaderboardId, uniqueIdentifier, score);
    }

    @Override
    protected void onPostExecute(Response<SavedScore> response) {
        // invoke callback if specified
        if (callback != null)
            callback.onComplete(response);
    }
}
