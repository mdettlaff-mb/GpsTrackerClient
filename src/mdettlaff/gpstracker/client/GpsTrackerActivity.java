package mdettlaff.gpstracker.client;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class GpsTrackerActivity extends Activity {

	private static final int GPS_MIN_TIME_IN_MILLIS = 3000;
	private static final int GPS_MIN_DISTANCE_IN_METERS = 15;
	private static final String TRACKING_ENABLED = "TRACKING_ENABLED";

	private LocationManager locationManager;
	private GpsTrackerDatabase database;
	private GpsLocationUploader uploader;
	private GpsTrackerListener listener;

	private Button startBtn;
	private Button stopBtn;
	private Button uploadBtn;
	private Button mapBtn;
	private Button clearBtn;
	private Button infoBtn;

	private boolean trackingEnabled;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_gps_tracker);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		GpsTrackerContext context = (GpsTrackerContext) getApplicationContext();
		database = context.getDatabase();
		uploader = context.getUploader();
		listener = new GpsTrackerListener(database);

		initState(savedInstanceState);
		initButtons();
		initTracking();
	}

	private void initState(Bundle savedInstanceState) {
		if (savedInstanceState != null) {
			trackingEnabled = savedInstanceState.getBoolean(TRACKING_ENABLED);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		outState.putBoolean(TRACKING_ENABLED, trackingEnabled);
	}

	private void initButtons() {
		startBtn = (Button) findViewById(R.id.startButton);
		startBtn.setEnabled(!trackingEnabled);
		startBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				startTracking();
				trackingEnabled = true;
				startBtn.setEnabled(false);
				stopBtn.setEnabled(true);
				showMessage("Tracking started");
			}
		});

		stopBtn = (Button) findViewById(R.id.stopButton);
		stopBtn.setEnabled(trackingEnabled);
		stopBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				stopTracking();
				trackingEnabled = false;
				startBtn.setEnabled(true);
				stopBtn.setEnabled(false);
				showMessage("Tracking stopped");
			}
		});

		uploadBtn = (Button) findViewById(R.id.uploadButton);
		uploadBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				uploadLocations();
			}
		});

		mapBtn = (Button) findViewById(R.id.mapButton);
		mapBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				Uri uri = Uri.parse(GpsTrackerContext.SERVER_BASE_URL);
				Intent openLinkIntent = new Intent(Intent.ACTION_VIEW, uri);
				startActivity(openLinkIntent);
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
				int locationsCount = database.countLocations();
				StringBuilder message = new StringBuilder();
				message.append("Number of locations to upload: ");
				message.append(locationsCount);
				message.append('\n');
				message.append("Tracking is ");
				message.append(trackingEnabled ? "enabled" : "disabled");
				showMessage(message.toString());
			}
		});
	}

	private void initTracking() {
		if (trackingEnabled) {
			startTracking();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (trackingEnabled) {
			stopTracking();
		}
	}

	private void startTracking() {
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
				GPS_MIN_TIME_IN_MILLIS, GPS_MIN_DISTANCE_IN_METERS, listener);
	}

	private void stopTracking() {
		locationManager.removeUpdates(listener);
	}

	private void uploadLocations() {
		if (database.hasLocations()) {
			GpsLocationUploaderTask uploaderTask = new GpsLocationUploaderTask(uploader, database,
					this);
			uploaderTask.execute();
		} else {
			showMessage("No tracking data to upload");
		}
	}

	void showMessage(String message) {
		Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
	}
}
