package rpc;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import db.DBConnectionFactory;
import entity.Event;
import db.DBConnection;

/**
 * Servlet implementation class EventHistory
 */
@WebServlet("/history")
public class EventHistory extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EventHistory() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String userId = request.getParameter("user_id");
		JSONArray array = new JSONArray();
		
		DBConnection conn = DBConnectionFactory.getConnection();
		try {
			Set<Event> events = conn.getFavorites(userId);
			for (Event  event : events) {
				JSONObject obj = event.toJSONObject();
				obj.append("favorite", true);
				array.put(obj);
			}
			
			RpcHelper.writeJsonArray(response, array);
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			conn.cleanUp();
		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBConnection db = DBConnectionFactory.getConnection();
		JSONObject  obj = RpcHelper.readJsonObject(request);
		try {
			String userId = obj.getString("user_id");
			// obtain favorite event ids
			JSONArray favorites = obj.getJSONArray("favorite");
			List<String> eventIds = new ArrayList<>();
			for (int i = 0; i < favorites.length(); i++) {
				eventIds.add(favorites.getString(i));
			}
			// update database
			db.setFavorites(userId, eventIds);
			// give response
			RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));
			
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			// close the connection
			db.cleanUp();
		}
	}

	/**
	 * @see HttpServlet#doDelete(HttpServletRequest, HttpServletResponse)
	 */
	protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBConnection db = DBConnectionFactory.getConnection();
		JSONObject  obj = RpcHelper.readJsonObject(request);
		try {
			String userId = obj.getString("user_id");
			// obtain favorite event ids
			JSONArray favorites = obj.getJSONArray("favorite");
			List<String> eventIds = new ArrayList<>();
			for (int i = 0; i < favorites.length(); i++) {
				eventIds.add(favorites.getString(i));
			}
			// update database
			db.unsetFavorites(userId, eventIds);
			// give response
			RpcHelper.writeJsonObject(response, new JSONObject().put("result", "SUCCESS"));
			
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			// close the connection
			db.cleanUp();
		}
	}

}
