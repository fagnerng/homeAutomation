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
	 * This method is a callback from the asyncRequest function that request an user after his login
	 * @param result
	 */
	public void onFinishRequestUser(User result);
	
	/**
	 * This method is a callback from the async request function that requests all root user's child from the application
	 * @param result
	 */
	public void onFinishRequestChilds(ArrayList<User> result);
}
