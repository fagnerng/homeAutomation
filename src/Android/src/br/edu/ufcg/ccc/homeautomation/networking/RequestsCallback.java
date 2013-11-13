package br.edu.ufcg.ccc.homeautomation.networking;

import java.util.ArrayList;

import android.app.Application;
import android.graphics.drawable.Drawable;
import br.edu.ufcg.ccc.homeautomation.entities.User;

/**
 * @author Bruno Paiva
 *
 */
public interface RequestsCallback {

	/**
	 * This method is a callback from the async request function that requests all users from the application
	 * @param result
	 */
	public void onFinish(ArrayList<User> result);
	
	public void onFinishRequestUser(User result);

// =======================================================================================================================
/*	
	*//**
	 * This method is a callback from the async request function
	 * @param result
	 *//*
	public void onFinishUpdate(ArrayList<String> result);
	
	*//**
	 * This method is a callback from the async request function
	 * @param result
	 *//*
	public void onFinish(Drawable result, int id);

	*//**
	 * This method is a callback from the async request function that
	 * requests a specific application details
	 * @param result
	 *//*
	public void onFinish(Application result);

	*//**
	 * This method is a callback from the async request function that
	 * requests all applications that have an list of applications
	 * @param result
	 *//*
	public void onFinishUpdated(ArrayList<Application> result);
	
	public void onFinish(Boolean result);

*/
}
