package br.edu.ufcg.ccc.homeautomation.managers;

import java.util.ArrayList;

import android.content.Context;
import br.edu.ufcg.ccc.homeautomation.entities.Device;
import br.edu.ufcg.ccc.homeautomation.networking.AsyncRequestChild;
import br.edu.ufcg.ccc.homeautomation.networking.AsyncRequestChildCRUD;
import br.edu.ufcg.ccc.homeautomation.networking.AsyncRequestEdit;
import br.edu.ufcg.ccc.homeautomation.networking.AsyncRequestLogin;
import br.edu.ufcg.ccc.homeautomation.networking.AsyncRequestSwitch;
import br.edu.ufcg.ccc.homeautomation.networking.AsyncRequestToken;
import br.edu.ufcg.ccc.homeautomation.networking.RequestsCallback;

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
    public static final String URL_DEFAULT = "http://192.168.2.30:9000/";
    public static final String URL_GET_TOKEN = URL_DEFAULT + "alogin";
    public static final String URL_GET_CHILD = URL_DEFAULT + "achild";
    public static final String URL_GET_USER = URL_DEFAULT + "auser";
    public static final String URL_POST_DEVICE = URL_DEFAULT + "adevice";
	
	/**
	 * This method is responsible for request user data from the server nodeJS
	 * 
	 * @param appCb - Type: RequestsCallback
	 * @param user - Type: String
	 */
	public void requestLogin(Context context){
		new AsyncRequestLogin(context).execute();
	}
	
	public void requestToken(String pass){
		new AsyncRequestToken(pass).execute();
	}
	
	public void requestEdit(RequestsCallback appCb, String name, String email, String pass, double lat, double lon){
		new AsyncRequestEdit(appCb, name, email, pass, lat, lon).execute();
	}
	
	public void requestChild(RequestsCallback appCb, String userChild){
		new AsyncRequestChild(appCb, userChild).execute();
	}
	
	public void requestChildUpdate(RequestsCallback appCb, String child, ArrayList<Integer> devices){
		new AsyncRequestChildCRUD(appCb, child, devices).execute();
	}
	
	public void requestChildCreate(RequestsCallback appCb, String child, ArrayList<Integer> devices, String name, String email, String pass, String house){
		new AsyncRequestChildCRUD(appCb, child, devices, name, email, pass, house).execute();
	}
	
	public void requestChildDelete(RequestsCallback appCb, String child){
		new AsyncRequestChildCRUD(appCb, child).execute();
	}
	
	public void requestSwitch(RequestsCallback appCb, Device device){
		new AsyncRequestSwitch(appCb, device).execute();
	}
}
