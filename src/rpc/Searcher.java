package rpc;

import java.io.IOException;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import entity.Event;
import external.TicketMasterAPI;
import db.DBConnection;
import db.DBConnectionFactory;


/**
 * Servlet implementation class Searcher
 * endpoint: search
 * @author: Jiayue Bao
 */
@WebServlet("/search")
public class Searcher extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Searcher() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		
		HttpSession session  = request.getSession(false);
		if (session == null) {
			response.setStatus(403);
			return;
		}
		String userId = session.getAttribute("user_id").toString();
		
		
		
		double latitude = Double.parseDouble(request.getParameter("lat"));
		double longitude = Double.parseDouble(request.getParameter("lon"));
		String term = request.getParameter("term");
		
		DBConnection db = DBConnectionFactory.getConnection();
		
		// search from ticketmaster API
		List<Event> events = db.searchEvent(latitude, longitude, term);
		Set<String> favoriteEvents = db.getFavoriteIds(userId);
		JSONArray array = new JSONArray();
		for (Event event : events) {
			JSONObject obj = event.toJSONObject();
			try {
				obj.put("favorite", favoriteEvents.contains(event.getId()));
			} catch (JSONException e) {
				e.printStackTrace();
			}
			array.put(obj);
		}
		RpcHelper.writeJsonArray(response, array);
	
		db.cleanUp();
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
