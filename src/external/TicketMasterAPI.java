package external;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Obtain "events" json data by Ticketmaster API
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
	 * @return json array of events data
	 */
	public JSONArray searchEvents(double latitude, double longitude, String keyword) {
		if (keyword == null) {
			keyword = DEFAULT_KEYWORD;
		}
		
		try {
			keyword = URLEncoder.encode(keyword, "UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		String query = String.format("apikey=%s&lat=%s&lon%s&keyword=%s&radius=%s&size=1", 
				API_KEY, latitude, longitude, keyword, RADIUS);
		
		String url = URL + "?" + query;
		
		try {
			// build HTTP connection
			HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
			connection.setRequestMethod("GET");
			
			int responseCode = connection.getResponseCode();
			System.out.println("request: " + url);
			
			// check response code
			if (responseCode != 200) {
				System.out.println(responseCode + " Error!");
				return new JSONArray();
			}
			
			// read the json response from Ticketmaster 
			BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String line;
			StringBuilder builder = new StringBuilder();
			while ((line = reader.readLine()) != null) {
				builder.append(line);
			}		
			reader.close();	
			
			// obtain events data
			JSONObject json =  new JSONObject(builder.toString());
			if (!json.isNull("_embedded")) {
				JSONObject embeddedObj = json.getJSONObject("_embedded");
				return embeddedObj.getJSONArray("events");
			}
			
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		return new JSONArray();
	}
	
	/**
	 * Test the query
	 * @param args
	 */
	public static void main(String[] args) {
		double latitude = 40.4406;
		double longitude = 79.9959;
		String keyword = "music";
		JSONArray jsonArray = new TicketMasterAPI().searchEvents(latitude, longitude, keyword);
		for (int i = 0; i < jsonArray.length(); i++) {
			try {
				JSONObject jsonObj = jsonArray.getJSONObject(i);
				System.out.println(jsonObj);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}

}
