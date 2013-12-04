package br.edu.ufcg.ccc.homeautomation.entities;

import org.json.JSONObject;

public class Child extends User {

	public Child(JSONObject json) {
		super(json);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public boolean isAdmin() {
		return false;
	}

}
