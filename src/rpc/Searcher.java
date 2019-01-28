package rpc;

import java.io.IOException;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		double latitude = Double.parseDouble(request.getParameter("lat"));
		double longitude = Double.parseDouble(request.getParameter("lon"));
		
		//TicketMasterAPI api = new TicketMasterAPI();
		//List<Event> events = api.fetchData(latitude, longitude, null);
		
		// fetch TicketMaster data via database api
		DBConnection db = DBConnectionFactory.getConnection();
		List<Event> events = db.searchEvent(latitude, longitude, null);
		
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
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
