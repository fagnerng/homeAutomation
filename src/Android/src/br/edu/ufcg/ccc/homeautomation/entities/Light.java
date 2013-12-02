package br.edu.ufcg.ccc.homeautomation.entities;

import org.json.JSONObject;

import br.edu.ufcg.ccc.homeautomation.R;

public class Light extends Device {
	
	public Light(JSONObject json) {
		super(json);
		
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int iconOn = R.drawable.light_on;
	private int iconOff = R.drawable.light_off;
	
	@Override
	int getIconID() {
		if (super.status){
			return iconOn;
		}
		return iconOff;
	}

	@Override
	public String getStatus() {
		if(super.status){
			return "Ligado";
		}
		return "Desligado";
	}

}
