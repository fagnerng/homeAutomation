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
    
    public static final String URL_GET_USER = "http://192.168.2.18:9000/getUser";
	public static final String URL_GET_ALL_USERS = "http://192.168.2.18:9000/getAllUsers";
	public static final String URL_GET_ALL_DEVICES = "http://192.168.2.18:9000/getAllDevices";
	
	/**
	 * This method uses the NetWorkManager to request an URL and the result is passed to the 
	 * JsonParser to execute the parseApplications and get an ArrayList containing the Applications
	 */	
	public void requestApplications(RequestsCallback appCb, int from, int quantity) {
		new AsyncRequests(appCb, from, quantity).execute();	
	}
	
	public void requestUser(){
		
	}
	
}
