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
	public void onFinish(ArrayList<User> result) {
	}
	
/*
	@Override
	public void onFinish(Drawable result, int id) {
	}

	@Override
	public void onFinish(Application result) {
	}

	@Override
	public void onFinishUpdated(ArrayList<Application> result) {
	}

	@Override
	public void onFinishUpdate(ArrayList<String> result) {
	}
	
	@Override
	public void onFinish(Boolean result){
	}
*/
}
