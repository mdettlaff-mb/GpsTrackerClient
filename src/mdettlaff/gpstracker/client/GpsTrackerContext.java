package mdettlaff.gpstracker.client;

import java.net.URI;

import android.app.Application;

public class GpsTrackerContext extends Application {

	public static final String SERVER_BASE_URL = "http://gpstrackerserver.herokuapp.com";
	// public static final String SERVER_BASE_URL = "http://10.0.3.2:8080";

	private static final URI SERVER_LOCATION_UPLOAD_URL = URI.create(SERVER_BASE_URL
			+ "/location/list");

	private final GpsTrackerDatabase database;
	private final GpsLocationUploader uploader;

	private boolean trackingEnabled;

	public GpsTrackerContext() {
		database = new GpsTrackerDatabase();
		uploader = new GpsLocationUploader(SERVER_LOCATION_UPLOAD_URL);
	}

	public GpsTrackerDatabase getDatabase() {
		return database;
	}

	public GpsLocationUploader getUploader() {
		return uploader;
	}

	public boolean isTrackingEnabled() {
		return trackingEnabled;
	}

	public void setTrackingEnabled(boolean trackingEnabled) {
		this.trackingEnabled = trackingEnabled;
	}
}
