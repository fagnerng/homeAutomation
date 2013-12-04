package br.edu.ufcg.ccc.homeautomation.entities;

import org.json.JSONObject;

public class Root extends User {

	public Root(JSONObject json) {
		super(json);
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean isAdmin() {
		return true;
	}

}
