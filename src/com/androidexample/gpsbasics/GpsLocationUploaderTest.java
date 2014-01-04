package com.androidexample.gpsbasics;

import java.net.URI;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GpsLocationUploaderTest {

	public static void main(String[] args) {
		URI url = URI.create("http://localhost:8080/location/list");
		List<GpsLocation> locations = new ArrayList<GpsLocation>();
		locations.add(createLocation(47.3763, 8.5547));
		locations.add(createLocation(47.3762, 8.5548));
		GpsLocationUploader uploader = new GpsLocationUploader(url);
		boolean result = uploader.upload(locations);
		System.out.println("result: " + result);
	}

	private static GpsLocation createLocation(double latitude, double longitude) {
		GpsLocation location = new GpsLocation();
		location.setLatitude(latitude);
		location.setLongitude(longitude);
		location.setTime(new Date());
		return location;
	}
}
