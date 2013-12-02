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
    public static final String URL_DEFAULT = "http://192.168.2.28:9000/";
    public static final String URL_GET_TOKEN = URL_DEFAULT + "alogin";
    public static final String URL_GET_CHILD = URL_DEFAULT + "achild";
    public static final String URL_GET_USER = URL_DEFAULT +"auser";
	
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
//		new AsyncRequestEdit(appCb).execute();
	}
	
}
