package rpc;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Helper class writing JSONArray/JSONObject to response
 * @author Jiayue Bao
 *
 */
public class RpcHelper {
	// Writes a JSONArray to HTTP response.
	public static void writeJsonArray(HttpServletResponse response, JSONArray array) throws IOException {
		response.setContentType("application/json");
		response.setHeader("Access-Control-Allow-Access", "*");
		PrintWriter out = response.getWriter();
		out.print(array);
		out.close();
		
	}
	
	// Write a JSONObject to HTTP response.
	public static void writeJsonObject(HttpServletResponse response, JSONObject obj) throws IOException {
		response.setContentType("application/json");
		response.setHeader("Access-Control-Allow-Access", "*");
		PrintWriter out = response.getWriter();
		out.print(obj);
		out.close();
	}
	
	// Read the request and convert to JSONObject and convert 
	public static JSONObject readJsonObject(HttpServletRequest request) {
		StringBuilder sBuilder = new StringBuilder();
	  	   try (BufferedReader reader = request.getReader()) {
	  		 String line = null;
	  		 while((line = reader.readLine()) != null) {
	  			 sBuilder.append(line);
	  		 }
	  		 return new JSONObject(sBuilder.toString());
	  		
	  	   } catch (Exception e) {
	  		 e.printStackTrace();
	  	   }
	  	
	  	  return new JSONObject();

	   }

}
