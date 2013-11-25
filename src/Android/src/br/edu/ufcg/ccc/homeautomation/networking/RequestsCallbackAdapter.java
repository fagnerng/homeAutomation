package br.edu.ufcg.ccc.homeautomation.networking;

import java.util.ArrayList;

import android.app.Application;
import android.graphics.drawable.Drawable;
import br.edu.ufcg.ccc.homeautomation.entities.User;

/**
 * @author Bruno Paiva
 * 
 * Adapter class to implement the Async's Callback functions
 */
public abstract class RequestsCallbackAdapter implements
		RequestsCallback {

	@Override
	public void  onFinishRequestChilds(ArrayList<User> result){
	}
	
}
