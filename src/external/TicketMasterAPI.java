package external;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entity.Record;
import entity.Record.RecordBuilder;

/**
 * Obtain "events" JSON data from Ticketmaster API
 * @author Jiayue Bao
 *
 */
public class TicketMasterAPI {
	private static final String URL = "https://app.ticketmaster.com/discovery/v2/events.json";
	private static final String DEFAULT_KEYWORD = ""; // no restriction
	private static final String API_KEY = "RuOMgaUhECOsVsKuTvEG3uSOgfhbIqcQ";
	private static final String RADIUS = "50";
	
	/**
	 * Given latitude, longitude and keyword, 
	 * do the query and obtain events data from the response;
	 * if the connection fails or the events field is null, return an empty json array 
	 * 
	 * @param latitude
	 * @param longitude
	 * @param keyword
	 * @return JSON array of events data
	 */
	public List<Record> fetchData(double latitude, double longitude, String keyword) {
		if (keyword == null) {
			keyword = DEFAULT_KEYWORD;
		}
		
		try {
			keyword = URLEncoder.encode(keyword, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String query = String.format("apikey=%s&lat=%s&lon%s&keyword=%s&radius=%s&size=10", 
				API_KEY, latitude, longitude, keyword, RADIUS);
		
		String url = URL + "?" + query;
		
		try {
			// build HTTP connection
			HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
			connection.setRequestMethod("GET");
			
			int responseCode = connection.getResponseCode(); // start to connect to ticketmaster API
			System.out.println("request: " + url);
			
			// check response code
			if (responseCode != 200) {
				System.out.println(responseCode + " Error!");
				return new ArrayList<>();
			}
			
			// read the JSON response from Ticketmaster API
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			StringBuilder builder = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}		
			reader.close();	
			
			// obtain events JSON array
			JSONObject json =  new JSONObject(builder.toString());
			if (!json.isNull("_embedded")) {
				JSONObject embedded = json.getJSONObject("_embedded");
				JSONArray events = embedded.getJSONArray("events");
				return getRecordList(events);
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		// obtain nothing
		return new ArrayList<Record>();
	}
	
	/**
	 * 
	 * @param events
	 * @return record list
	 * @throws JSONException
	 */
	private List<Record> getRecordList(JSONArray events) throws JSONException {
		List<Record> records = new ArrayList<>();
		for (int i = 0; i < events.length(); i++) {
			JSONObject event = events.getJSONObject(i);
			Record record = filterData(event);
			records.add(record);
		}
		return records;
	}
	
	/**
	 * 
	 * @param event
	 * @return filtered data: record
	 * @throws JSONException
	 */
	private Record filterData(JSONObject event) throws JSONException {
		RecordBuilder builder = new RecordBuilder();
		if (!event.isNull("id")) {
			builder.setId(event.getString("id"));
		} else {
			builder.setId("");
		}
		if (!event.isNull("name")) {
			builder.setName(event.getString("name"));
		} else {
			builder.setName("");
		}
		if (!event.isNull("url")) {
			builder.setUrl(event.getString("url"));
		} else {
			builder.setUrl("");
		}
		if (!event.isNull("distance")) {
			builder.setDistance(event.getDouble("distance"));
		} 
		
		builder.setAddress(getAddress(event));
		builder.setClassifications(getClassifications(event));
		builder.setImageUrl(getImageUrl(event));
		
		return builder.build();
	}
	
	/**
	 * 
	 * @param event
	 * @return address 
	 * @throws JSONException
	 */
	private String getAddress(JSONObject event) throws JSONException {
		if (!event.isNull("_embedded")) {
			JSONObject embedded = event.getJSONObject("_embedded");
			if (!embedded.isNull("venues")) {
				JSONArray venues = embedded.getJSONArray("venues");
				for (int i = 0; i < venues.length(); i++) {
					JSONObject venue = venues.getJSONObject(i);
					StringBuilder builder = new StringBuilder();
					if (!venue.isNull("address")) {
						JSONObject address = venue.getJSONObject("address");
						if (!address.isNull("line1")) {
							builder.append(address.getString("line1"));
							builder.append(",");
						}
						if (!address.isNull("line2")) {
							builder.append(address.getString("line2"));
							builder.append(",");
						}
						if (!address.isNull("line3")) {
							builder.append(address.getString("line3"));
							builder.append(",");
						}
					}
					if (!venue.isNull("city")) {
						JSONObject city = venue.getJSONObject("city");
						if (!city.isNull("name")) {
							builder.append(city.getString("name"));
						}
					}
					String str = builder.toString();
					if (!str.equals("")) {
						return str;
					}
				}
			}
		}
		return "";
	}
	
	/**
	 * 
	 * @param event
	 * @return classifications set
	 * @throws JSONException
	 */
	private Set<String> getClassifications(JSONObject event) throws JSONException {
		Set<String> set = new HashSet<>();
		if (!event.isNull("classifications")) {
			JSONArray classifications = event.getJSONArray("classifications");
			for (int i = 0; i < classifications.length(); i++) {
				JSONObject classification = classifications.getJSONObject(i);
				if (!classification.isNull("segment")) {
					JSONObject segment = classification.getJSONObject("segment");
					if (!segment.isNull("name")) {
						set.add(segment.getString("name"));
					}
				}
			}
		}
		return set;
	}
	
	/**
	 * 
	 * @param event
	 * @return imageUrl
	 * @throws JSONException
	 */
	private String getImageUrl(JSONObject event) throws JSONException {
		if (!event.isNull("images")) {
			JSONArray images = event.getJSONArray("images");
			for (int i = 0; i < images.length(); i++) {
				JSONObject image = images.getJSONObject(i);
				if (!image.isNull("url")) {
					return image.getString("url");
				}
			}
		}
		return "";
	}
	/**
	 * Test the query
	 * @param args
	 */
	public static void main(String[] args) {
		double latitude = 40.4406;
		double longitude = 79.9959;
		String keyword = "music";
		List<Record> events = new TicketMasterAPI().fetchData(latitude, longitude, keyword);
		
		for (Record event : events) {
			System.out.println(event.toJSONObject());
		}

	}

}
