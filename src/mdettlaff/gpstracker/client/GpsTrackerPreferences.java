package mdettlaff.gpstracker.client;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

public class GpsTrackerPreferences {

	private static final String TRACKING_ENABLED = "TRACKING_ENABLED";

	private final SharedPreferences preferences;

	public GpsTrackerPreferences(Activity activity) {
		preferences = activity.getPreferences(Context.MODE_PRIVATE);
	}

	public boolean isTrackingEnabled() {
		return preferences.getBoolean(TRACKING_ENABLED, false);
	}

	public void setTrackingEnabled(boolean trackingEnabled) {
		preferences.edit().putBoolean(TRACKING_ENABLED, trackingEnabled).commit();
	}
}
