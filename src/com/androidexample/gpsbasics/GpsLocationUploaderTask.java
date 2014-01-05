package com.androidexample.gpsbasics;

import java.util.List;

import android.os.AsyncTask;

public class GpsLocationUploaderTask extends AsyncTask<Void, Long, Boolean> {

	private GpsLocationUploader uploader;
	private GpsBasicsAndroidExample activity;
	private GpsTrackerDatabase database;

	public GpsLocationUploaderTask(GpsLocationUploader uploader, GpsTrackerDatabase database,
			GpsBasicsAndroidExample activity) {
		this.uploader = uploader;
		this.database = database;
		this.activity = activity;
	}

	@Override
	protected Boolean doInBackground(Void... args) {
		List<GpsLocation> locations = database.getLocations();
		boolean uploadedSuccessfully = uploader.upload(locations);
		return uploadedSuccessfully;
	}

	@Override
	protected void onPostExecute(Boolean uploadedSuccessfully) {
		if (uploadedSuccessfully) {
			database.clear();
			activity.showMessage("Tracking data uploaded successfully");
		} else {
			activity.showMessage("Cannot upload tracking data");
		}
	}
}
