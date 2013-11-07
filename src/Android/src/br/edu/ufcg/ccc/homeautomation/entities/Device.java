package br.edu.ufcg.ccc.homeautomation.entities;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Bruno Paiva
 *
 *	This Class will represent the Device from the homeAutomation user
 *	Esta Classe ira represnetar o Dispositivo do usuário do homeAutomation
 *
 */
public class Device {
	
	private String name;
	private String id;


	/**
	 * @param json
	 * 	
	 * The Entity User will be created from an JSONObject received by the request to the NodeJS server
	 * A Entidade Usuário sera criada a partir de um JSONObject recebido através de uma requisição ao servidor nodeJS
	 * 
	 */
	public Device(JSONObject json){
		try {
			this.name = json.getString("name");
			this.id = json.getString("email");
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * Getters and Setters
	 * 
	 */
	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getId() {
		return id;
	}


	public void setId(String id) {
		this.id = id;
	}
	
	
}