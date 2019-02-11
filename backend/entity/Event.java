package entity;

import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Event {
	private final String id;
	private final String name;
	private final String address;
	private final Set<String> categories;
	private final String imageUrl;
	private final String url;
	private final double distance;
	
	private Event(EventBuilder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.address = builder.address;
		this.categories = builder.categories;
		this.imageUrl = builder.imageUrl;
		this.url = builder.url;
		this.distance = builder.distance;
	}
	
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("item_id", id);
			obj.put("name", name);
			obj.put("address", address);
			obj.put("categories", new JSONArray(categories));
			obj.put("image_url", imageUrl);
			obj.put("url", url);
			obj.put("distance", distance);

		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	public String getId() {
		return id;
	}


	public String getName() {
		return name;
	}

	public String getAddress() {
		return address;
	}


	public Set<String> getCategories() {
		return categories;
	}


	public String getImageUrl() {
		return imageUrl;
	}


	public String getUrl() {
		return url;
	}


	public double getDistance() {
		return distance;
	}


	/** inner class */
	public static class EventBuilder {
		private String id;
		private String name;
		private String address;
		private Set<String> categories;
		private String imageUrl;
		private String url;
		private double distance;
		
		/* external interface */
		public Event build() {
			return new Event(this);
		}
		
		public void setId(String id) {
			this.id = id;
		}
		public void setName(String name) {
			this.name = name;
		}
		public void setAddress(String address) {
			this.address = address;
		}
		public void setCategories(Set<String> categories) {
			this.categories = categories;
		}
		public void setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public void setDistance(double distance) {
			this.distance = distance;
		}
		
	}
}
