package mdettlaff.gpstracker.client;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

public class GpsTrackerDatabase {

	private static final String FILENAME = "locations";

	private final Context context;
	private final ObjectMapper mapper;

	public GpsTrackerDatabase(Context context) {
		this.context = context;
		mapper = new ObjectMapper();
	}

	public void saveLocation(Location location) {
		try {
			FileOutputStream output = context.openFileOutput(FILENAME, Context.MODE_APPEND);
			try {
				output.write(mapper.writeValueAsBytes(convertLocation(location)));
				output.write("\n".getBytes());
			} finally {
				output.close();
			}
		} catch (IOException e) {
			Log.e("GPS Tracker database", "Cannot save location.", e);
		}
	}

	private GpsLocation convertLocation(Location original) {
		GpsLocation result = new GpsLocation();
		result.setLatitude(original.getLatitude());
		result.setLongitude(original.getLongitude());
		result.setTime(new Date(original.getTime()));
		result.setAccuracy(original.getAccuracy());
		result.setAltitude(original.getAltitude());
		result.setBearing(original.getBearing());
		result.setSpeed(original.getSpeed());
		return result;
	}

	public void clear() {
		context.deleteFile(FILENAME);
	}

	public List<GpsLocation> getLocations() {
		List<GpsLocation> locations = new ArrayList<GpsLocation>();
		try {
			FileInputStream input = context.openFileInput(FILENAME);
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				for (String line; (line = reader.readLine()) != null;) {
					GpsLocation location = mapper.readValue(line, GpsLocation.class);
					locations.add(location);
				}
			} finally {
				input.close();
			}
		} catch (FileNotFoundException e) {
			// should return empty list if file not found
		} catch (IOException e) {
			Log.e("GPS Tracker database", "Cannot read locations.", e);
		}
		return locations;
	}

	public int countLocations() {
		int locationCount = 0;
		try {
			FileInputStream input = context.openFileInput(FILENAME);
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(input));
				while (reader.readLine() != null) {
					locationCount++;
				}
			} finally {
				input.close();
			}
		} catch (FileNotFoundException e) {
			// should return 0 if file not found
		} catch (IOException e) {
			Log.e("GPS Tracker database", "Cannot read locations for counting.", e);
		}
		return locationCount;
	}

	public boolean hasLocations() {
		return countLocations() > 0;
	}
}
