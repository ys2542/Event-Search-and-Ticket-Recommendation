package external;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import entity.Item;

public class TicketMasterAPI {
	private static final String URL = "https://app.ticketmaster.com/discovery/v2/events.json";
	private static final String DEFAULT_KEYWORD = ""; // no restriction
	private static final String API_KEY = "jzWZkAbMtDKzyGNBr2gu5TowsinIDULj";

	public List<Item> search(double lat, double lon, String keyword) {		
		if (keyword == null) {
			keyword = DEFAULT_KEYWORD;
		}
		
		try {
			keyword = URLEncoder.encode(keyword, "UTF-8");  //"Rick Sun" => "Rick%20Sun"
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
		String geoHash = GeoHash.encodeGeohash(lat, lon, 8);
		String query = String.format("apikey=%s&geoPoint=%s&keyword=%s&radius=%s", API_KEY, geoHash, keyword, 50);
		//String query = String.format("apikey=%s&latlong=%s,%s&keyword=%s&radius=%s", API_KEY, lat, lon, keyword, 50);
		String url = URL + "?" + query;
		
		try {
			HttpURLConnection connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setRequestMethod("GET");
			int responseCode = connection.getResponseCode();
			System.out.println("Sending request to url:" + url);
			System.out.println("Response code: " + responseCode);

			if (responseCode != 200) {
				return new ArrayList<Item>();
			}
			
			BufferedReader reader = new BufferedReader(new 
					InputStreamReader(connection.getInputStream()));
			String line;
			StringBuilder response = new StringBuilder();
			
			while ((line = reader.readLine()) != null) {
				response.append(line);
			}
			reader.close();
			JSONObject obj = new JSONObject(response.toString());
			
			if (!obj.isNull("_embedded")) {
				JSONObject embedded = obj.getJSONObject("_embedded");
			    return getItemList(embedded.getJSONArray("events"));

			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ArrayList<Item>();
	}
	
	private List<Item> getItemList(JSONArray events) throws JSONException {
		List<Item> itemList = new ArrayList<>();
		for (int i = 0; i < events.length(); ++i) {
			JSONObject event = events.getJSONObject(i);
			
			Item.ItemBuilder builder = new Item.ItemBuilder();
			if (!event.isNull("id")) {
				builder.setItemId(event.getString("id"));
			}
			if (!event.isNull("name")) {
				builder.setName(event.getString("name"));
			}
			if (!event.isNull("url")) {
				builder.setUrl(event.getString("url"));
			}
			if (!event.isNull("distance")) {
				builder.setDistance(event.getDouble("distance"));
			}
			
			builder.setAddress(getAddress(event))
				.setCategories(getCategories(event))
				.setImageUrl(getImageUrl(event));
			
			itemList.add(builder.build());
		}
		return itemList;
	}
	
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
						}
						
						if (!address.isNull("line2")) {
							builder.append(",");
							builder.append(address.getString("line2"));
						}
						
						if (!address.isNull("line3")) {
							builder.append(",");
							builder.append(address.getString("line3"));
						}
					}
					
					if (!venue.isNull("city")) {
						JSONObject city = venue.getJSONObject("city");
						builder.append(",");
						builder.append(city.getString("name"));
					}
					
					String result = builder.toString();
					if (!result.isEmpty()) {
						return result;
					}
				}	
			}	
		}
		return "";
	}
	
	private String getImageUrl(JSONObject event) throws JSONException {
		if (!event.isNull("images")) {
			JSONArray array = event.getJSONArray("images");
			for (int i = 0; i < array.length(); i++) {
				JSONObject image = array.getJSONObject(i);
				if (!image.isNull("url")) {
					return image.getString("url");
				}
			}
		}
		return "";
	}

	private Set<String> getCategories(JSONObject event) throws JSONException {
		Set<String> categories = new HashSet<>();
		if (!event.isNull("classifications")) {
			JSONArray classifications = event.getJSONArray("classifications");
			for (int i = 0; i < classifications.length(); ++i) {
				JSONObject classification = classifications.getJSONObject(i);
				if (!classification.isNull("segment")) {
					JSONObject segment = classification.getJSONObject("segment");
					if (!segment.isNull("name")) {
						categories.add(segment.getString("name"));
					}
				}
			}
		}
		return categories;		
		
	}
	
	
	private void queryAPI(double lat, double lon) {
		List<Item> events = search(lat, lon, null);

		try {
			for (Item event : events) {
				System.out.println(event.toJSONObject());
			}
		} catch (Exception e) {
	                  e.printStackTrace();
		}
	}
	

	/**
	 * Main entry for sample TicketMaster API requests.
	 */ 
	
	public static void main(String[] args) {
		TicketMasterAPI tmApi = new TicketMasterAPI();
		// Mountain View, CA
		tmApi.queryAPI(37.38, -122.08);
		// London, UK
		// tmApi.queryAPI(51.503364, -0.12);
		// Houston, TX
		//tmApi.queryAPI(29.682684, -95.295410);
	}
	
}
