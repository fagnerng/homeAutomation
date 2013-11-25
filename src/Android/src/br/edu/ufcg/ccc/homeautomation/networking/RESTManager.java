package br.edu.ufcg.ccc.homeautomation.networking;

/**
 * @author Bruno Paiva
 * 
 * RESTManager class is responsible for managing the Server APIs which will be
 * used on the Store
 */
public class RESTManager {
	
    private static RESTManager instance = new RESTManager();
    
    private RESTManager() {
    }

    public static synchronized RESTManager getInstance() {
        return instance;
    }
    
    // Remember to change the IP ADDRESS to the server IP
    public static final String URL_GET_USER = "http://192.168.2.30:9000/auser";
    public static final String URL_GET_TOKEN = "http://192.168.2.30:9000/alogin";
	
	/**
	 * This method is responsible for request user data from the server nodeJS
	 * 
	 * @param appCb - Type: RequestsCallback
	 * @param user - Type: String
	 */
	public void requestUser(RequestsCallback appCb, String user, String pass){
		new AsyncRequestUser(appCb, user, pass).execute();
	}
	
	public void requestEdit(RequestsCallback appCb){
		new AsyncRequestEdit(appCb).execute();
	}
	
}
