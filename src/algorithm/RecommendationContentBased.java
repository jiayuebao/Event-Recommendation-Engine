package algorithm;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Event;


public class RecommendationContentBased {
	public List<Event> recommend(String userId, double lat, double lon) {
		List<Event> results = new ArrayList<>();
		
		// step1 : get all favorite eventIds
		DBConnection db = DBConnectionFactory.getConnection();
		Set<String> favoriteEventIds = db.getFavoriteIds(userId);
		
		// step2 : get all categories, sort by count
		Map<String, Integer> map = new HashMap<>(); // key: category value: count
		for (String eventId : favoriteEventIds) {
			Set<String> categories = db.getCategories(eventId);
			for (String category : categories) {
				map.put(category, map.getOrDefault(category, 0)+1);
			}
		}
		List<String> keyList = new ArrayList<>(map.keySet()); 
		Collections.sort(keyList, (k1, k2) -> (map.get(k2)-map.get(k1)));
		
		
		// step3: search based on category, filter out favorite events
		Set<String> visited = new HashSet<>();
		for (String key : keyList) {
			List<Event> events = db.searchEvent(lat, lon, key);
			
			for (Event event: events) {
				if (!favoriteEventIds.contains(event.getId()) && !visited.add(event.getId())) {
					results.add(event);
				}
			}
		}
		db.cleanUp();
		return results;
	}

}
