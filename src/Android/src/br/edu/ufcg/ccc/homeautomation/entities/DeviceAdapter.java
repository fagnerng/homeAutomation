package br.edu.ufcg.ccc.homeautomation.entities;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.sax.StartElementListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import br.edu.ufcg.ccc.homeautomation.DeviceEdit;
import br.edu.ufcg.ccc.homeautomation.GuideActivityRoot;
import br.edu.ufcg.ccc.homeautomation.R;
import br.edu.ufcg.ccc.homeautomation.R.string;
import br.edu.ufcg.ccc.homeautomation.managers.RESTManager;
import br.edu.ufcg.ccc.homeautomation.managers.UserManager;
import br.edu.ufcg.ccc.homeautomation.networking.RequestsCallback;
import br.edu.ufcg.ccc.homeautomation.networking.RequestsCallbackAdapter;

public class DeviceAdapter extends BaseAdapter{
	private List<Device> mDevices;
	private LayoutInflater mInflater;
	private ImageButton editDevice;
	private ImageButton buttonStatus;
	private NumberPicker pickerTimer;
	private Switch switchTimer;
	private Drawable on;
	private Drawable off;
	private Device mDev;
	
	
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
		final String devName = dev.getName();
		tv_name.setText(devName);
		
		final Drawable on = view.getResources().getDrawable(R.drawable.switch_icon_on);
		final Drawable off = view.getResources().getDrawable(R.drawable.switch_icon_off);
		
		TextView tv_status = (TextView) view.findViewById(R.id.tv_status_device);
		String status = dev.getStringStatus();
		tv_status.setText(status);
		
		final Context c = view.getContext();
		final Intent i = new Intent(c,DeviceEdit.class);
		i.putExtra("dev", dev);
		
		final ImageButton ib_edit = (ImageButton) view.findViewById(R.id.ib_icon_edit);
		
		ib_edit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.getContext().startActivity(i);
				//callDialogEditDevie(v, mDev);
				
			}
		});
		
		final ImageButton ib_switch = (ImageButton) view.findViewById(R.id.ib_icon_switch);
		
		if(dev.status){
			ib_switch.setImageDrawable(on);
		}else{
			ib_switch.setImageDrawable(off);
		}
		
		final Animation press = AnimationUtils.loadAnimation(view.getContext(),R.anim.reversed_scale);
		
		ib_switch.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.startAnimation(press);
				executeSwitchPower(v, dev, ib_switch,on,off);
				
			}
		});
		
		return view;
	}
	
	private void executeSwitchPower(final View v,final Device device, final ImageButton ib_switch, final Drawable on, final Drawable off){
		
		RESTManager.getInstance().requestSwitch(new RequestsCallbackAdapter() {
			
			@Override
			public void onFinishRequestSwitch(Boolean result) {
				System.out.println("RESULT: " + result);
				
				Drawable drawable = null;
				
				if(result){
					if (device.getStatus()){
						ib_switch.setImageDrawable(off);
						
					}else{
						ib_switch.setImageDrawable(on);
					}
					device.setStatus(!(device.getStatus()));
				}else{
					Toast.makeText(v.getContext(), "Impossivel mudar Status do device", Toast.LENGTH_SHORT).show();
				}
			}
		}, device);
	}
	
	private void executeDeviceEdit(final View v, final Device d){
		Intent i = new Intent(v.getContext(),DeviceEdit.class);
		//i.putExtra("device", d);
		v.getContext().startActivity(i);
		//Toast.makeText(v.getContext(), "Abrir Device Edit Screen", Toast.LENGTH_SHORT).show();
	}
	
	private void callDialogEditDevie(final View v, final Device d){
		final Dialog dialogEditDevice = new Dialog(v.getContext(),R.style.myCoolDialog);
		dialogEditDevice.setContentView(R.layout.teste_activity_device_edit);
		
		on = dialogEditDevice.getContext().getResources().getDrawable(R.drawable.switch_icon_on_big);
		off = dialogEditDevice.getContext().getResources().getDrawable(R.drawable.switch_icon_off_big);
		mDev = d;
		editDevice = (ImageButton) dialogEditDevice.findViewById(R.id.button_edit_device);
		buttonStatus = (ImageButton) dialogEditDevice.findViewById(R.id.button_status_config);
		if(mDev.getStatus()){
			buttonStatus.setImageDrawable(on);
		}else{
			buttonStatus.setImageDrawable(off);
		}
		
		if(!(UserManager.getInstance().getUserObject().isAdmin())){
			editDevice.setVisibility(ImageButton.INVISIBLE);
			editDevice.setClickable(false);
		}
		
		final TextView deviceName = (TextView) dialogEditDevice.findViewById(R.id.tv_device_name);
		
		editDevice.setOnClickListener	(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showEditDevices(v,mDev,deviceName );
				
			}

			
		});
		
	}
	
	
	
	
	
	private void showEditDevices(View v, final Device dev, final TextView mView) {
		final Dialog edit = new Dialog(v.getContext());
		edit.setContentView(R.layout.dialog_edit_device);
		edit.setTitle("Renomear Dispositivo");
		Button cancel = (Button) edit.findViewById(R.id.button_cancel_device);
		Button confirm = (Button) edit.findViewById(R.id.button_confirm_device);
		final EditText editName = (EditText) edit.findViewById(R.id.edit_device_name);
		edit.show();
		confirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dev.setName(editName.getText().toString());
				mView.setText(dev.getName());
				
				edit.dismiss();
			}
		});
		cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				edit.dismiss();
				
			}
		});
		editName.setText(dev.getName());
		
		
		
		

		
	}

}

