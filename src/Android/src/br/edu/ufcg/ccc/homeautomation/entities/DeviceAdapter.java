package br.edu.ufcg.ccc.homeautomation.entities;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import br.edu.ufcg.ccc.homeautomation.R;
import br.edu.ufcg.ccc.homeautomation.R.string;
import br.edu.ufcg.ccc.homeautomation.managers.RESTManager;
import br.edu.ufcg.ccc.homeautomation.networking.RequestsCallback;
import br.edu.ufcg.ccc.homeautomation.networking.RequestsCallbackAdapter;

public class DeviceAdapter extends BaseAdapter{
	private List<Device> mDevices;
	private LayoutInflater mInflater;
	
	public DeviceAdapter (Context context, List<Device> devs){
		mInflater = LayoutInflater.from(context);
		mDevices = devs;
	}
	
	@Override
	public int getCount() {
		return mDevices.size();
	}

	@Override
	public Object getItem(int index) {
		return mDevices.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}

	@Override
	public View getView(int posicao, View view, ViewGroup viewGroup) {
		
		view = mInflater.inflate(R.layout.device_adapter, null);
		final Device dev = mDevices.get(posicao);
		
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name_device);
		String devName = dev.getName();
		tv_name.setText(devName);
		
		TextView tv_status = (TextView) view.findViewById(R.id.tv_status_device);
		String status = dev.getStringStatus();
		tv_status.setText(status);
		
		final ImageButton ib_edit = (ImageButton) view.findViewById(R.id.ib_icon_edit);
		
		ib_edit.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				executeDeviceEdit(v);
			}
		});
		
		final ImageButton ib_switch = (ImageButton) view.findViewById(R.id.ib_icon_switch);
		
		ib_switch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				executeSwitchPower(v, dev, ib_switch);
			}
		});
		
		return view;
	}
	
	private void executeSwitchPower(final View v,final Device device, final ImageButton ib_switch){
		
		RESTManager.getInstance().requestSwitch(new RequestsCallbackAdapter() {
			
			@Override
			public void onFinishRequestSwitch(Boolean result) {
				System.out.println("RESULT: " + result);
				
				Drawable drawable = null;
				
				if(result){
					if (device.getStatus()){
						drawable = v.getResources().getDrawable(R.drawable.switch_icon_off);
						ib_switch.setImageDrawable(drawable);
					}else{
						drawable = v.getResources().getDrawable(R.drawable.switch_icon_on);
						ib_switch.setImageDrawable(drawable);
					}
				}else{
					Toast.makeText(v.getContext(), "Impossivel mudar Status do device", Toast.LENGTH_SHORT).show();
				}
			}
		}, device);
	}
	
	private void executeDeviceEdit(final View v){
		Toast.makeText(v.getContext(), "Abrir Device Edit Screen", Toast.LENGTH_SHORT).show();
	}

}

