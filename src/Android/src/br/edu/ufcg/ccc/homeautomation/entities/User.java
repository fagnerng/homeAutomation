package br.edu.ufcg.ccc.homeautomation.entities;

import java.io.Serializable;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import br.edu.ufcg.ccc.homeautomation.managers.UserManager;

/**
 * @author Bruno Paiva
 * 
 *         This Class will represent the User from the homeAutomation app Esta
 *         Classe ira represnetar o Usuario da aplicação homeAutomation
 * 
 */
public abstract class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final String TAG_DEVICES = "devices";

	private String user;
	private String token;
	private String name;
	private String email;
	private String house;
	private boolean admin;
	private double lat;
	private double lon;

	private ArrayList<Device> devices;

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
		System.out.println("json received: "+json.toString());
		if (json != null) {

			JSONArray devs = null;
			try {
				this.user = json.getString("user");

				devs = json.getJSONArray(TAG_DEVICES);
				try {
					for (int i = 0; i < devs.length(); i++) {
						devices.add(UserManager.getInstance().getUserObject()
								.getDevices()
								.get((Integer) devs.get(i)));
					}

				} catch (Exception e) {
					for (int i = 0; i < devs.length(); i++) {

						if (devs.getJSONObject(i).getString("type")
								.equals("airCondition")) {
							devices.add(new AirCondition(devs.getJSONObject(i)));
						} else {
							devices.add(new Light(devs.getJSONObject(i)));
						}
					}
				}

				this.name = json.getString("name");
				this.email = json.getString("email");
				this.user = json.getString("user");
				this.house = json.getString("house");
				this.lat = json.getDouble("lati");
				this.lon = json.getDouble("long");
				this.token = json.getString("token");

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
	abstract public boolean isAdmin();

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

	public String getToken() {
		return token;
	}

	public void setToken(String currentToken) {
//		this.token = currentToken;
		System.out.println("Passou, " + currentToken);
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

	@Override
	public String toString() {
		return "User [name=" + this.name + ", email=" + this.email + ", user=" + this.user
				+ ", house=" + this.house + ", token=" + this.token + ", admin="
				+ this.admin + ", lat=" + this.lat + ", lon=" + this.lon + ", devices="
				+ this.devices + "]";
	}
}