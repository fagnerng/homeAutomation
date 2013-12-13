package br.edu.ufcg.ccc.homeautomation.entities;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import br.edu.ufcg.ccc.homeautomation.DeviceEditActivity;
import br.edu.ufcg.ccc.homeautomation.R;
import br.edu.ufcg.ccc.homeautomation.managers.RESTManager;
import br.edu.ufcg.ccc.homeautomation.managers.UserManager;
import br.edu.ufcg.ccc.homeautomation.networking.RequestsCallbackAdapter;

public class DeviceAdapter extends BaseAdapter{
	private List<Device> mDevices;
	private LayoutInflater mInflater;
	private ImageButton editDevice;
	private ImageButton buttonStatus;
//	private NumberPicker pickerTimer;
//	private Switch switchTimer;
	private Drawable on;
	private Drawable off;
	private Device mDev;	
	private Spinner timer;
	private SharedPreferences prefs;
	
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
		prefs = PreferenceManager.getDefaultSharedPreferences(view.getContext());
		
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name_device);
		final String devName = dev.getName();
		tv_name.setText(devName);
		
		final Drawable on = view.getResources().getDrawable(R.drawable.switch_icon_on);
		final Drawable off = view.getResources().getDrawable(R.drawable.switch_icon_off);
		
		TextView tv_status = (TextView) view.findViewById(R.id.tv_status_device);
		String status = dev.getStringStatus();
		tv_status.setText(status);
		
		final Context c = view.getContext();
		final Intent i = new Intent(c,DeviceEditActivity.class);
		i.putExtra("dev", dev);
		
		final ImageButton ib_edit = (ImageButton) view.findViewById(R.id.ib_icon_edit);
		
		ib_edit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
//				v.getContext().startActivity(i);
				callDialogEditDevie(v, dev);
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
				
			//	Drawable drawable = null;
				
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
	
//	private void executeDeviceEdit(final View v, final Device d){
//		Intent i = new Intent(v.getContext(),DeviceEdit.class);
//		//i.putExtra("device", d);
//		v.getContext().startActivity(i);
//		//Toast.makeText(v.getContext(), "Abrir Device Edit Screen", Toast.LENGTH_SHORT).show();
//	}
	
	private void callDialogEditDevie(final View v, final Device d){
		final Dialog dialogEditDevice = new Dialog(v.getContext(), R.style.myCoolDialog);
		
		Button bt_Confirm = null;
		Button bt_Cancel = null;
		
		if (d.getType().equals("light")){
			dialogEditDevice.setContentView(R.layout.dialog_light_edit);
			bt_Confirm = (Button) dialogEditDevice.findViewById(R.id.light_confirm);
			bt_Cancel = (Button) dialogEditDevice.findViewById(R.id.light_cancel);
		}else{
			dialogEditDevice.setContentView(R.layout.dialog_aircond_edit);
			bt_Confirm = (Button) dialogEditDevice.findViewById(R.id.air_confirm);
			bt_Cancel = (Button) dialogEditDevice.findViewById(R.id.air_cancel);
		}		
		
		on = dialogEditDevice.getContext().getResources().getDrawable(R.drawable.switch_icon_on);
		off = dialogEditDevice.getContext().getResources().getDrawable(R.drawable.switch_icon_off);
		editDevice = (ImageButton) dialogEditDevice.findViewById(R.id.button_edit_device);
		buttonStatus = (ImageButton) dialogEditDevice.findViewById(R.id.button_status_config);
		mDev = d;
		timer = (Spinner) dialogEditDevice.findViewById(R.id.spinner_default_times);
		Integer times[] = {1,5,10,15,30,60,90,20};
	//	Integer temperaturas[] = {17,18,19,20,21,22,23,24,25};
		Integer timerDefault = Integer.valueOf(prefs.getString("timer_default", "2"));

		
		
//		if(!mDev.getTimer().equals("333")){
//			int indice = findItem(times, mDev.getTimer());
//			if(indice != -1){
//				timer.setSelection(indice);
//			}
//		}else{
			timer.setSelection(findItem(times, String.valueOf(timerDefault)));
		//}
		
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
		deviceName.setText(mDev.getName());
		
		dialogEditDevice.setTitle(dialogEditDevice.getContext().getResources().getString(R.string.config_devices));
		dialogEditDevice.show();
		
		editDevice.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showEditDevices(v, mDev, deviceName);				
			}			
		});
		
		final Animation animationPress = AnimationUtils.loadAnimation(v.getContext(),R.anim.reversed_scale);
		
		buttonStatus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.startAnimation(animationPress);
				executeSwitchPower(v, mDev, buttonStatus, on,off);
//				Toast.makeText(v.getContext(), "Chamar funcionalidade do Switch", Toast.LENGTH_SHORT).show();
			}			
		});
		
		bt_Confirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				notifyDataSetChanged();
				Toast.makeText(v.getContext(), "Requisicao Confirmar", Toast.LENGTH_SHORT).show();
				dialogEditDevice.dismiss();
			}
		});
		
		bt_Cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				notifyDataSetChanged();
				dialogEditDevice.dismiss();
			}
		});
		
	}
	
	private void showEditDevices(View v, final Device dev, final TextView mView) {
		final Dialog edit = new Dialog(v.getContext());
		edit.setContentView(R.layout.dialog_edit_device);
		edit.setTitle(edit.getContext().getResources().getString(R.string.rename_device));
		Button cancel = (Button) edit.findViewById(R.id.button_cancel_device);
		Button confirm = (Button) edit.findViewById(R.id.button_confirm_device);
		final EditText editName = (EditText) edit.findViewById(R.id.edit_device_name);
		edit.show();
		
		confirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!editName.getText().toString().trim().equals("")){
					dev.setName(editName.getText().toString());
					mView.setText(dev.getName());
					edit.dismiss();
				}else{
					Toast.makeText(v.getContext(), v.getResources().getString(R.string.empty_name), Toast.LENGTH_SHORT).show();
				}
				
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
	
	private int findItem(Integer[] array, String term){
		for (int i = 0; i < array.length; i++) {
			if(String.valueOf(array[i]).equals(term)){
				return i;
			}
		}
		
		return -1;
		
	}

}

