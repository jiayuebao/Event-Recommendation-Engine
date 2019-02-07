package db;

import java.util.List;
import java.util.Set;

import entity.Event;

public interface DBConnection {
	/**
	 * Close the connection.
	 */
	public void cleanUp();

	/**
	 * Insert the favorite event for a user.
	 * 
	 * @param userId
	 * @param eventIds
	 */
	public void setFavorites(String userId, List<String> eventIds);

	/**
	 * Delete the favorite event for a user.
	 * 
	 * @param userId
	 * @param eventIds
	 */
	public void unsetFavorites(String userId, List<String> eventIds);

	/**
	 * Get the favorite event id for a user.
	 * 
	 * @param userId
	 * @return eventIds
	 */
	public Set<String> getFavoriteIds(String userId);

	/**
	 * Get the favorite event for a user.
	 * 
	 * @param userId
	 * @return event
	 */
	public Set<Event> getFavorites(String userId);

	/**
	 * Gets classifications based on event id
	 * 
	 * @param eventId
	 * @return set of classifications
	 */
	public Set<String> getCategories(String eventId);

	/**
	 * Search events near a geolocation and a term (optional).
	 * 
	 * @param userId
	 * @param latxf
	 * @param lon
	 * @param term
	 *            (Nullable)
	 * @return list of events
	 */
	public List<Event> searchEvent(double lat, double lon, String term);

	/**
	 * Save event into db.
	 * 
	 * @param event
	 */
	public void saveEvent(Event event);

	/**
	 * Get full name of a user. (This is not needed for main course, just for demo
	 * and extension).
	 * 
	 * @param userId
	 * @return full name of the user
	 */
	public String getUserName(String userId);

	/**
	 * Return whether the credential is correct. (This is not needed for main
	 * course, just for demo and extension)
	 * 
	 * @param userId
	 * @param password
	 * @return boolean
	 */
	public boolean verifyLogin(String userId, String password);
	
	/**
	 * Register one user.
	 * @param userId
	 * @param password
	 * @param firstname
	 * @param lastname
	 * @return boolean
	 */
	
	public boolean registerUser(String userId, String password, String firstname, String lastname);
}

