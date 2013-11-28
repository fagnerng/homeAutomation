package br.edu.ufcg.ccc.homeautomation.entities;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Bruno Paiva
 * 
 *         This Class will represent the User from the homeAutomation app Esta
 *         Classe ira represnetar o Usuario da aplicação homeAutomation
 * 
 */
public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String TAG_DEVICES = "devices";

	private String name;
	private String email;
	private String user;
	private String house;
	private String currentToken;
	private boolean admin;
	private double lat;
	private double lon;

	private ArrayList<Device> devices;

	@Override
	public String toString() {
		return "User [name=" + name + ", email=" + email + ", user=" + user
				+ ", house=" + house + ", currentToken=" + currentToken
				+ ", admin=" + admin + ", lat=" + lat + ", lon=" + lon
				+ ", devices=" + devices + "]";
	}

	/**
	 * @param json
	 * 
	 *            The Entity User will be created from an JSONObject received by
	 *            the request to the NodeJS server A Entidade Usuário sera
	 *            criada a partir de um JSONObject recebido através de uma
	 *            requisição ao servidor nodeJS
	 * 
	 */
	public User(JSONObject json) {
		devices = new ArrayList<Device>();

		if (json != null) {

			JSONArray devs = null;
			try {
				this.name = json.getString("name");
				this.email = json.getString("email");
				this.user = json.getString("user");
				this.house = json.getString("house");
				this.admin = json.getBoolean("admin");
				this.lat = json.getDouble("lati");
				this.lon = json.getDouble("long");
			
				this.currentToken = json.getString("token");
				devs = json.getJSONArray(TAG_DEVICES);
				for (int i = 0; i < devs.length(); i++) {
					devices.add(new Device(devs.getJSONObject(i)));
				}

			} catch (JSONException e) {
				// e.printStackTrace();
			}
		}

	}

	/**
	 * 
	 * Getters and Setters
	 * 
	 */
	public boolean isAdmin() {
		return admin;
	}

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

	public String getHouse() {
		return house;
	}

	public void setHouse(String house) {
		this.house = house;
	}

	public ArrayList<Device> getDevices() {
		return this.devices;
	}

	public void setDevices(ArrayList<Device> devices) {
		this.devices = devices;
	}

	public String getCurrentToken() {
		return currentToken;
	}

	public void setCurrentToken(String currentToken) {
		this.currentToken = currentToken;
	}

	public double getLatitude() {
		return lat;
	}

	public void setLatitude(double lat) {
		this.lat = lat;
	}

	public double getLongitude() {
		return lon;
	}

	public void setLongitude(double lon) {
		this.lon = lon;
	}
}
