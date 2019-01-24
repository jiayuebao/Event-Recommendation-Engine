package entity;

import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Record {
	private final String id;
	private final String name;
	private final String address;
	private final Set<String> classifications;
	private final String imageUrl;
	private final String url;
	private final double distance;
	
	private Record(RecordBuilder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.address = builder.address;
		this.classifications = builder.classifications;
		this.imageUrl = builder.imageUrl;
		this.url = builder.url;
		this.distance = builder.distance;
	}
	
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("id", id);
			obj.put("name", name);
			obj.put("address", address);
			obj.put("classifications", new JSONArray(classifications));
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


	public Set<String> getClassifications() {
		return classifications;
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
	public static class RecordBuilder {
		private String id;
		private String name;
		private String address;
		private Set<String> classifications;
		private String imageUrl;
		private String url;
		private double distance;
		
		/* external interface */
		public Record build() {
			return new Record(this);
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
		public void setClassifications(Set<String> classifications) {
			this.classifications = classifications;
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
