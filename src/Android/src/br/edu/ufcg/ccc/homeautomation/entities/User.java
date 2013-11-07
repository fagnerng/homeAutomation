package br.edu.ufcg.ccc.homeautomation.entities;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Bruno Paiva
 *
 *	This Class will represent the User from the homeAutomation app
 *	Esta Classe ira represnetar o Usuario da aplicação homeAutomation
 *
 */
public class User {
	
	private String name;
	private String email;
	private String user;
	private String pass;
	private String house;
	private boolean admin;
	
	private ArrayList<Device> devices;
	
	/**
	 * @param json
	 * 	
	 * The Entity User will be created from an JSONObject received by the request to the NodeJS server
	 * A Entidade Usuário sera criada a partir de um JSONObject recebido através de uma requisição ao servidor nodeJS
	 * 
	 */
	public User(JSONObject json){
		if (json != null){
		
			try {
				this.name = json.getString("name");
				this.email = json.getString("email");
				this.user = json.getString("user");
				this.pass = json.getString("pass");
				this.house = json.getString("house");
				this.admin = json.getBoolean("admin");
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getHouse() {
		return house;
	}

	public void setHouse(String house) {
		this.house = house;
	}

	public ArrayList<Device> getDevices(){
		return this.devices;
	}
	
	public void setDevices(ArrayList<Device> devices) {
		this.devices = devices;
	}

}
