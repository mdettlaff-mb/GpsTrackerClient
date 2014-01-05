package mdettlaff.gpstracker.client;

import android.app.Activity;
import android.content.Context;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class GpsTrackerActivity extends Activity {

	private static final int GPS_MIN_TIME_IN_MILLIS = 5000;
	private static final int GPS_MIN_DISTANCE_IN_METERS = 15;

	private LocationManager locationManager;
	private GpsTrackerContext context;
	private GpsTrackerDatabase database;
	private GpsLocationUploader uploader;
	private GpsTrackerListener listener;

	private Button startBtn;
	private Button stopBtn;
	private Button uploadBtn;
	private Button clearBtn;
	private Button infoBtn;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_gps_tracker);

		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		context = (GpsTrackerContext) getApplicationContext();
		database = context.getDatabase();
		uploader = context.getUploader();
		listener = new GpsTrackerListener(database);

		initButtonListeners();
		initTracking();
	}

	private void initButtonListeners() {
		startBtn = (Button) findViewById(R.id.startButton);
		startBtn.setEnabled(!context.isTrackingEnabled());
		startBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				startTracking();
				context.setTrackingEnabled(true);
				startBtn.setEnabled(false);
				stopBtn.setEnabled(true);
				showMessage("Tracking started");
			}
		});

		stopBtn = (Button) findViewById(R.id.stopButton);
		stopBtn.setEnabled(context.isTrackingEnabled());
		stopBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				stopTracking();
				context.setTrackingEnabled(false);
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
				StringBuilder message = new StringBuilder();
				message.append("Number of locations to upload: " + locationsCount + "\n");
				message.append("Tracking is "
						+ (context.isTrackingEnabled() ? "enabled" : "disabled"));
				showMessage(message.toString());
			}
		});
	}

	private void initTracking() {
		if (context.isTrackingEnabled()) {
			startTracking();
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (context.isTrackingEnabled()) {
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
			GpsLocationUploaderTask uploadTask = new GpsLocationUploaderTask(uploader, database,
					this);
			uploadTask.execute();
		} else {
			showMessage("No tracking data to upload");
		}
	}

	void showMessage(String message) {
		Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
	}
}
