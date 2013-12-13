package br.edu.ufcg.ccc.homeautomation.entities;

import org.json.JSONException;
import org.json.JSONObject;

import br.edu.ufcg.ccc.homeautomation.R;

public class Light extends Device {
	
	public Light(JSONObject json) {
		super(json);
	}

	private static final long serialVersionUID = 1L;
	private int iconOn = R.drawable.light_on;
	private int iconOff = R.drawable.light_off;
	
	@Override
	public int getIconID() {
		if (super.status){
			return iconOn;
		}
		return iconOff;
	}

	@Override
	public String getStringStatus() {
		if(super.status){
			return "Ligado";
		}
		return "Desligado";
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
		}catch(JSONException e){
		
		}
		return json;
	}

}
