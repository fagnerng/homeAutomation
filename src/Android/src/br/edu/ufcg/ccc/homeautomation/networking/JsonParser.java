package br.edu.ufcg.ccc.homeautomation.networking;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Comment;

import android.app.Application;
import br.edu.ufcg.ccc.homeautomation.entities.Device;
import br.edu.ufcg.ccc.homeautomation.entities.User;

/**
 * @author Bruno Paiva
 * 
 * JSONParser class is responsible for create all JSON objects received by the server
 *
 */
public class JsonParser {
	
	// JSON Node names
	private static final String TAG_NAME = "Name";
	private static final String TAG_EMAIL = "Email";
	private static final String TAG_USER = "User";
	private static final String TAG_PASS = "Pass";
	private static final String TAG_HOUSE = "House";
	
	private static final String TAG_DEVICES = "Devices";
			
	/**
	 * This method parses all users received by the Server at JSON format to User Objects
	 * 
	 * @param jsonText - type: String
	 * 
	 * @return An ArrayList containing all the users objects
	 */
	public static ArrayList<User> parseAllUsers(String jsonText) {
		
		ArrayList<User> users = new ArrayList<User>();
		
		if (jsonText == null)
			return users;
		
    	try {
    		//JSONObject json Verificar json que sera recebido
    		JSONArray json = new JSONArray(jsonText);
    		
    	    for(int i = 0; i < json.length(); i++) {
 
    	    	JSONObject c = json.getJSONObject(i);
    	    	
    	        User user = new User(c);
    	        
    	        ArrayList<Device> devices = new ArrayList<Device>();
    	        JSONArray tagDevices =  c.getJSONArray(TAG_DEVICES);
    	        
    	        for (int j=0; j<tagDevices.length(); j++){
    	        	JSONObject dev = tagDevices.getJSONObject(j);
    	        	devices.add(new Device(dev));
    	        }
    	        user.setDevices(devices); //Setting the User Devices
    	    }
    	} catch (JSONException e) {
    	    e.printStackTrace();
    	}
		
		return users;
	}
	
	public static User parseUser(String json){
		JSONObject jsonUser = null;
		
		try {
			jsonUser = new JSONObject(json);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		User user = new User(jsonUser);
		return user;		
		
	}
}
