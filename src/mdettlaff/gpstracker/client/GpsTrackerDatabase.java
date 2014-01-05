package mdettlaff.gpstracker.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.location.Location;

public class GpsTrackerDatabase {

	private List<GpsLocation> locations;

	public GpsTrackerDatabase() {
		locations = new ArrayList<GpsLocation>();
	}

	public void saveLocation(Location location) {
		locations.add(convertLocation(location));
	}

	private GpsLocation convertLocation(Location original) {
		GpsLocation result = new GpsLocation();
		result.setLatitude(original.getLatitude());
		result.setLongitude(original.getLongitude());
		result.setTime(new Date(original.getTime()));
		return result;
	}

	public void clear() {
		locations.clear();
	}

	public List<GpsLocation> getLocations() {
		return locations;
	}

	public boolean hasLocations() {
		return !locations.isEmpty();
	}
}
