package com.mogade.android;

import com.mogade.Guard;

import android.os.AsyncTask;

public class GetScoresTask extends AsyncTask<Void,Void,Void> {
	private String leaderboardId;
	private ScoreScope scope = ScoreScope.Daily;
	private int page = 1;
	private int records = 10;

	public GetScoresTask(String leaderboardId){
		Guard.NotNullOrEmpty(leaderboardId, "A valid leaderboard id is required.");
		
		this.leaderboardId = leaderboardId;
	}

	@Override
	protected Void doInBackground(Void... params) {
		
		return null;
	}

	public void setScope(ScoreScope scope) {
		this.scope = scope;
	}

	public ScoreScope getScope() {
		return scope;
	}

	public GetScoresTask setPage(int value) {
		Guard.GreaterThanZero(value, "Invalid parge. Page must be greater than zero.");
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

}
