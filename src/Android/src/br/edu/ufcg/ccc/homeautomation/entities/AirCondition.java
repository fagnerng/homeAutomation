package br.edu.ufcg.ccc.homeautomation.entities;

import org.json.JSONException;
import org.json.JSONObject;

import br.edu.ufcg.ccc.homeautomation.R;


public class AirCondition extends Device {
	private int temperature= 17;
	private int iconCool = R.drawable.air_cool;
	private int iconHot = R.drawable.air_hot;
	public AirCondition(JSONObject json) {
		super(json);
		try {
			this.temperature = json.getInt("temperature");
		}catch(JSONException e){

		}
	}

	public int getTemperature() {
		return temperature;
	}

	public void setTemperature(int temperature) {
		if(temperature> 30){
			this.temperature = 30;
		}
		else if(temperature< 17){
			this.temperature = 17;
		}
		else {
			this.temperature = temperature;
		}
	}
	public void increaseTemp(){
		if(this.temperature< 30){
			this.temperature++;
		}
	}
	public void decreaseTemp(){
		if (this.temperature> 17){
			this.temperature--;
		}
	}
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Override
	public int getIconID() {
		if (temperature < 25){
			return iconCool;
		}
		return iconHot;
	}

	@Override
	public String getStringStatus() {
		
		return temperature + " ºC";
	}

	@Override
	public JSONObject generateBody(JSONObject json) {
		try{
			json.put("devices", this.getId());
			if (this.timer > 0){
				json.put("timer", this.timer);
				json.put("status", true);
			}else{
				json.put("status", !this.status);
			}
			json.put("temperature", this.temperature);
		}catch(JSONException e){
		
		}
		return json;
	}

}
