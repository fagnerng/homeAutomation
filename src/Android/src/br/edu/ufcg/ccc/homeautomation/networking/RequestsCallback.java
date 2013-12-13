package br.edu.ufcg.ccc.homeautomation.networking;

import java.util.ArrayList;

import br.edu.ufcg.ccc.homeautomation.entities.User;

/**
 * @author Bruno Paiva
 *
 */
public interface RequestsCallback {

	/**
	 * This method is a callback from the asyncRequest function that request an user after his login
	 * @param User result
	 * The user that was requested at the login
	 */
	public void onFinishRequestUser(User result);
	
	/**
	 * This method is a callback from the async request function that requests the edition of a user
	 * @param Boolean result
	 */
	public void onFinishRequestEdit(Boolean result);
	
	/**
	 * This method is a callback from the async request function that requests all root user's child from the application
	 * @param ArrayList<User> result
	 * All Root's users child
	 */
	public void onFinishRequestChild(ArrayList<User> result);
	
	/**
	 * This method is a callback from the async request function that requests CRUD Methods over child
	 * @param Boolean result - if it occurs like plan
	 */
	public void onFinishRequestChildCRUD(Boolean result);
	
	/**
	 * This method is a callback from the async request function that changes the device status
	 * @param Boolean result - if it occurs like plan
	 */
	public void onFinishRequestSwitch(Boolean result);
	
}
