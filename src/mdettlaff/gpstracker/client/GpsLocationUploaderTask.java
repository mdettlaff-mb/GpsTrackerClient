package mdettlaff.gpstracker.client;

import java.util.List;

import android.os.AsyncTask;

public class GpsLocationUploaderTask extends AsyncTask<Void, Long, String> {

	private GpsLocationUploader uploader;
	private GpsTrackerActivity activity;
	private GpsTrackerDatabase database;

	public GpsLocationUploaderTask(GpsLocationUploader uploader, GpsTrackerDatabase database,
			GpsTrackerActivity activity) {
		this.uploader = uploader;
		this.database = database;
		this.activity = activity;
	}

	@Override
	protected String doInBackground(Void... args) {
		String errorMessage = null;
		try {
			List<GpsLocation> locations = database.getLocations();
			boolean uploadedSuccessfully = uploader.upload(locations);
			if (!uploadedSuccessfully) {
				errorMessage = "Cannot upload tracking data";
			}
		} catch (Exception e) {
			errorMessage = "Cannot upload tracking data (" + e + ")";
		}
		return errorMessage;
	}

	@Override
	protected void onPostExecute(String errorMessage) {
		if (errorMessage == null) {
			database.clear();
			activity.showMessage("Tracking data uploaded successfully");
		} else {
			activity.showMessage(errorMessage);
		}
	}
}
