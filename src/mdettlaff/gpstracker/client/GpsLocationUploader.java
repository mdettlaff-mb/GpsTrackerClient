package mdettlaff.gpstracker.client;

import java.net.URI;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

public class GpsLocationUploader {

	private final URI url;
	private RestTemplate restTemplate;

	public GpsLocationUploader(URI url) {
		this.url = url;
		restTemplate = new RestTemplate();
		restTemplate.getMessageConverters().add(new StringHttpMessageConverter());
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
	}

	public boolean upload(List<GpsLocation> locations) {
		ResponseEntity<String> entity = restTemplate.postForEntity(url, locations, String.class);
		return entity.getStatusCode() == HttpStatus.OK
				&& ObjectUtils.nullSafeEquals(entity.getBody(), "OK");
	}
}
