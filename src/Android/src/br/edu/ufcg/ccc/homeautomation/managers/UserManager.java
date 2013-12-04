package br.edu.ufcg.ccc.homeautomation.managers;

import org.json.JSONObject;

import br.edu.ufcg.ccc.homeautomation.entities.User;

/**
 * @author Bruno Paiva
 * 
 * UserManager class is responsible for managing the Server APIs which will be
 * used on the Store
 */
public class UserManager {
	
	private static UserManager instance = new UserManager();
	private User userObject;
	private String pass;
    
    private UserManager() {
    }

    public static synchronized UserManager getInstance() {
        return instance;
    }
    
    public String getUser(){
    	return userObject.getUser();
    }
    
    public String getToken(){
    	return userObject.getToken();
    }
    
    public User getUserObject(){
    	return userObject;
    }
    
    public void setUserObject(User userObj){
    	this.userObject = userObj;
    }
    
    public void setPass(String pass){
    	this.pass = pass;
    }
    
    public void setToken(String token){
    	this.userObject.setToken(token);
    }
    
    public void refreshToken(){
    	RESTManager.getInstance().requestToken(this.pass);
    }
    
    public JSONObject generateBody(){
    	JSONObject json = new JSONObject();
    	try{
    		json.put("user", this.getUser());
    		json.put("token", this.getToken());
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    	return json;
    }
}
