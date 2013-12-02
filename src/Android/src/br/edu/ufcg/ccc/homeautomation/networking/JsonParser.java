package br.edu.ufcg.ccc.homeautomation.networking;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.edu.ufcg.ccc.homeautomation.entities.Device;
import br.edu.ufcg.ccc.homeautomation.entities.Light;
import br.edu.ufcg.ccc.homeautomation.entities.Root;
import br.edu.ufcg.ccc.homeautomation.entities.User;

/**
 * @author Bruno Paiva
 * 
 * JSONParser class is responsible for create all JSON objects received by the server
 *
 */
public class JsonParser {
	
	// JSON Node names
/*	private static final String TAG_NAME = "Name";
	private static final String TAG_EMAIL = "Email";
	private static final String TAG_USER = "User";
	private static final String TAG_PASS = "Pass";
	private static final String TAG_HOUSE = "House";*/
	
	private static final String TAG_DEVICES = "Devices";
			
	/**
	 * This method parses all users received by the Server at JSON format to User Objects
	 * 
	 * @param jsonText - type: String
	 * 
	 * @return An ArrayList containing all the users objects
	 */
	public static ArrayList<User> parseAllChilds(String jsonText) {
		
		ArrayList<User> users = new ArrayList<User>();
		
		if (jsonText == null)
			return users;
		
    	try {
    		//JSONObject json Verificar json que sera recebido
    		JSONArray json = new JSONArray(jsonText);
    		
    	    for(int i = 0; i < json.length(); i++) {
 
    	    	JSONObject userJson = json.getJSONObject(i);
    	    	
    	        User user = new Root(userJson);

    	        ArrayList<Device> devices = new ArrayList<Device>();
    	        JSONArray tagDevices =  userJson.getJSONArray(TAG_DEVICES);
    	        
    	        for (int j=0; j<tagDevices.length(); j++){
    	        	JSONObject dev = tagDevices.getJSONObject(j);
    	        	devices.add(new Light(dev));
    	        }
    	        user.setDevices(devices); //Setting the User Devices
    	        users.add(user);
    	    }
    	} catch (JSONException e) {
    	    e.printStackTrace();
    	}
		
		return users;
	}
}
