package db.mysql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import db.DBConnection;
import entity.Event;
import entity.Event.EventBuilder;
import external.TicketMasterAPI;

public class MySQLConnection implements DBConnection {
	private Connection conn;
	
	public MySQLConnection() {
		try {
			Class.forName(MySQLInit.JDBC_DRIVER).getConstructor().newInstance();
			conn = DriverManager.getConnection(MySQLInit.URL);
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void cleanUp() {
		if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
	}

	@Override
	public void setFavorites(String userId, List<String> eventIds) {
		if (conn == null) {
	  		   System.err.println("MySQL Database connection failed");
	  		   return;
	  	 }
		String sql = "INSERT IGNORE INTO history(user_id, event_id) VALUES (?,?)";
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			for (String eventId: eventIds) {
				pstmt.setString(2, eventId);
				pstmt.executeUpdate();
				System.out.println("setFavorites");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void unsetFavorites(String userId, List<String> eventIds) {
		if (conn == null) {
	  		   System.err.println("MySQL Database connection failed");
	  		   return;
	  	 }
		String sql = "DELETE FROM history WHERE user_id = ? AND event_id = ?";
		System.out.println("Unset favorites");
		try {
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, userId);
			for (String eventId: eventIds) {
				pstmt.setString(2, eventId);
				pstmt.executeUpdate();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	@Override
	public Set<String> getFavoriteIds(String userId) {
		if (conn == null) {
			return new HashSet<String>();
		}
		
		Set<String> eventIds = new HashSet<>();
		try {
			String sql = "SELECT event_id FROM history WHERE user_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, userId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				eventIds.add(rs.getString("event_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return eventIds;
	}

	
	@Override
	public Set<Event> getFavorites(String userId) {
		if (conn == null) {
			return new HashSet<Event>();
		}
		
		Set<Event> favorites = new HashSet<>();
		Set<String> eventIds = getFavoriteIds(userId);
		
		try {
			String sql = "SELECT * FROM events WHERE event_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			for (String eventId : eventIds) {
				stmt.setString(1, eventId);
				
				ResultSet rs = stmt.executeQuery();
				EventBuilder builder = new EventBuilder();
				
				while (rs.next()) {
					builder.setId(rs.getString("event_id"));
					builder.setName(rs.getString("name"));
					builder.setAddress(rs.getString("address"));
					builder.setImageUrl(rs.getString("img_url"));
					builder.setUrl(rs.getString("url"));
					builder.setCategories(getCategories(eventId));
					builder.setDistance(rs.getDouble("distance"));
					
					favorites.add(builder.build());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return favorites;
	}

	@Override
	public Set<String> getCategories(String eventId) {
		if (conn == null) {
			return new HashSet<String>();
		}
		Set<String> categories = new HashSet<>();
		try {
			String sql = "SELECT category FROM categories WHERE event_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, eventId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				categories.add(rs.getString("event_id"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}

	@Override
	public List<Event> searchEvent(double lat, double lon, String keyword) {
		TicketMasterAPI api = new TicketMasterAPI();
		List<Event> list = api.fetchData(lat, lon, keyword);
		for (Event event : list) {
			saveEvent(event);
		}
		return list;
	}

	@Override
	public void saveEvent(Event event) {
		 if (conn == null) {
	  		   System.err.println("MySQL Database connection failed");
	  		   return;
	  	 }
		
		try {
			// save to events table
			String sql = "INSERT IGNORE INTO events VALUES (?,?,?,?,?,?)";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, event.getId());
			pstmt.setString(2, event.getName());
			pstmt.setString(3, event.getAddress());
			pstmt.setString(4, event.getImageUrl());
			pstmt.setString(5, event.getUrl());
			pstmt.setDouble(6, event.getDistance());
			pstmt.executeUpdate();
			
			// save to categories table
			sql = "INSERT IGNORE INTO categories VALUES (?,?)";
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, event.getId());
			
			for (String category : event.getCategories()) {
				pstmt.setString(2, category);
				pstmt.executeUpdate();
			}
			
			} catch (SQLException e) {
				e.printStackTrace();
			}
		
		}

	@Override
	public String getUserName(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean verifyLogin(String userId, String password) {
		// TODO Auto-generated method stub
		return false;
	}

}
