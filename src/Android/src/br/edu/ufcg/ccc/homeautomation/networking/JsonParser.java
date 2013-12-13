package br.edu.ufcg.ccc.homeautomation.networking;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.edu.ufcg.ccc.homeautomation.entities.Child;
import br.edu.ufcg.ccc.homeautomation.entities.User;

/**
 * @author Bruno Paiva
 * 
 * JSONParser class is responsible for create all JSON objects received by the server
 *
 */
public class JsonParser {
	/**
	 * This method parses all users received by the Server at JSON format to User Objects
	 * 
	 * @param jsonText - type: String
	 * 
	 * @return An ArrayList containing all the users objects
	 */
	public static ArrayList<User> parseAllChilds(String jsonText) {
		
		ArrayList<User> users = new ArrayList<User>();
		
		if (jsonText == null || jsonText.equals("err")){
			System.out.println("NULL!??");
			return users;
		}
		
    	try {
    		//JSONObject json Verificar json que sera recebido
    		JSONArray json = new JSONArray(jsonText);
    		
    	    for(int i = 0; i < json.length(); i++) {
 
    	    	JSONObject userJson = json.getJSONObject(i);
    	    	
    	        User user = new Child(userJson);
    	        
    	        System.out.println("USER CREATING:" + user.getName());
    	        users.add(user);
    	    }
    	} catch (JSONException e) {
    	    System.out.println("Exceção de json...");
    		e.printStackTrace();
    	}
		
		return users;
	}

	public static String parseRefreshToken(String result) {
    	try {

    		JSONObject json = new JSONObject(result);
    		return json.getString("token");
    		
    	}catch(JSONException e){
//    		e.printStackTrace();
    	}
    	
    	return null;
	}
}
