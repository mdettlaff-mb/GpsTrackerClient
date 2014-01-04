package com.androidexample.gpsbasics;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

public class GpsLocationUploader {

	private final URI url;
	private RestTemplate restTemplate;

	public GpsLocationUploader(URI url) {
		this.url = url;
		restTemplate = new RestTemplate();
	}

	public boolean upload(List<GpsLocation> locations) {
		ResponseEntity<String> entity = restTemplate.postForEntity(url, locations, String.class);
		return entity.getStatusCode() == HttpStatus.OK
				&& ObjectUtils.nullSafeEquals(entity.getBody(), "OK");
	}
}
