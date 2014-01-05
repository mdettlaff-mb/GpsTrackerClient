package mdettlaff.gpstracker.client;

import java.net.URI;

import com.androidexample.gpsbasics.R;

import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class GpsTrackerActivity extends Activity implements LocationListener {

	private static final URI SERVER_LOCATION_UPLOAD_URL = URI
	// .create("http://gpstracker.herokuapp.com/location/list");
			.create("http://10.0.3.2:8080/location/list");

	private LocationManager locationManager;
	private GpsTrackerDatabase database;
	private GpsLocationUploader uploader;
	private Button startBtn;
	private Button stopBtn;
	private Button uploadBtn;
	private Button clearBtn;
	private Button infoBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_gps_basics_android_example);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		database = new GpsTrackerDatabase();
		uploader = new GpsLocationUploader(SERVER_LOCATION_UPLOAD_URL);

		initButtonListeners();
	}

	private void initButtonListeners() {
		startBtn = (Button) findViewById(R.id.startButton);
		startBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 15,
						GpsTrackerActivity.this);
				showMessage("Tracking started");
				startBtn.setEnabled(false);
				stopBtn.setEnabled(true);
			}
		});

		stopBtn = (Button) findViewById(R.id.stopButton);
		stopBtn.setEnabled(false);
		stopBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				locationManager.removeUpdates(GpsTrackerActivity.this);
				showMessage("Tracking stopped");
				startBtn.setEnabled(true);
				stopBtn.setEnabled(false);
			}
		});

		uploadBtn = (Button) findViewById(R.id.uploadButton);
		uploadBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				uploadLocations();
			}
		});

		clearBtn = (Button) findViewById(R.id.clearButton);
		clearBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				database.clear();
				showMessage("Database cleared successfully");
			}
		});

		infoBtn = (Button) findViewById(R.id.infoButton);
		infoBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				int locationsCount = database.getLocations().size();
				showMessage("Number of locations to upload: " + locationsCount);
			}
		});
	}

	private void uploadLocations() {
		if (database.hasLocations()) {
			GpsLocationUploaderTask uploadTask = new GpsLocationUploaderTask(uploader, database,
					this);
			uploadTask.execute();
		} else {
			showMessage("No tracking data to upload");
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		database.saveLocation(location);
		showLocationMessage(location);
	}

	private void showLocationMessage(Location location) {
		showMessage("Latitude: " + location.getLatitude() + " \nLongitude: "
				+ location.getLongitude());
	}

	@Override
	public void onProviderDisabled(String provider) {
		showMessage("GPS turned off");
	}

	@Override
	public void onProviderEnabled(String provider) {
		showMessage("GPS turned on");
	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	void showMessage(String message) {
		Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
	}
}
