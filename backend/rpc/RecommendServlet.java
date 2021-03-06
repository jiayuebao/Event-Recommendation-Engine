package rpc;

import java.io.IOException;
import java.io.PrintWriter;

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

import algorithm.RecommendationContentBased;
import db.DBConnection;
import db.DBConnectionFactory;
import external.TicketMasterAPI;
import entity.Event;

/**
 * Servlet implementation class Recommender
 * endpoint: recommendation
 * @author: Jiayue Bao
 */
@WebServlet("/recommendation")
public class RecommendServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RecommendServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session  = request.getSession(false);
		if (session == null) {
			response.setStatus(403);
			return;
		}
		
		String userId = session.getAttribute("user_id").toString();
		double latitude = Double.parseDouble(request.getParameter("lat"));
		double longitude = Double.parseDouble(request.getParameter("lon"));
		
		// search for recommendation
		RecommendationContentBased recommendation = new RecommendationContentBased();
		List<Event> events = recommendation.recommend(userId, latitude, longitude);
		
		DBConnection db = DBConnectionFactory.getConnection();
		JSONArray array = new JSONArray();
		for (Event event : events) {
			array.put(event.toJSONObject());
		}
		
		RpcHelper.writeJsonArray(response, array);
	
		db.cleanUp();
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
