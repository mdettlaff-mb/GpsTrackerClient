package mdettlaff.gpstracker.client;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;

public class GpsTrackerListener implements LocationListener {

	private final GpsTrackerDatabase database;

	public GpsTrackerListener(GpsTrackerDatabase database) {
		this.database = database;
	}

	@Override
	public void onLocationChanged(Location location) {
		database.saveLocation(location);
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}
}
