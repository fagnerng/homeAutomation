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
	private int id;
	private boolean status;
	private String type;
	
	/**
	 * @param json
	 * 	
	 * The Entity User will be created from an JSONObject received by the request to the NodeJS server
	 * A Entidade Usuário sera criada a partir de um JSONObject recebido através de uma requisição ao servidor nodeJS
	 * 
	 */
	public Device(JSONObject json){
		if (json != null){
			
			try {
				this.name = json.getString("name");
				this.id = json.getInt("id");
				this.status = json.getBoolean("status");
				this.type = json.getString("type");
			} catch (JSONException e) {
				e.printStackTrace();
			}
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean getStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}
	
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}