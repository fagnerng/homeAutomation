package br.edu.ufcg.ccc.homeautomation.networking;

/**
 * @author Bruno Paiva
 * 
 * RESTManager class is responsible for managing the Server APIs which will be
 * used on the Store
 */
public class RESTManager {
	
    private static RESTManager instance = new RESTManager();
//    private static String token;
    
    private RESTManager() {
    }

    public static synchronized RESTManager getInstance() {
        return instance;
    }
    
//    public static final String URL_GET_USER = "http://192.168.2.28:9000/alogin?"; //user=brunoffp&pass=brunoffp";
//    public static final String URL_GET_TOKEN = "http://localhost:9000/agetuser?user=fagnerng&token=PdxsNL1gVTeV" 
    
//    public static final String URL_GET_USER = "http://localhost:9000/agetuser";
//    public static final String URL_GET_TOKEN = "http://localhost:9000/alogin";
    
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
	
}
