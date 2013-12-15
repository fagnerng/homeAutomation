package br.edu.ufcg.ccc.homeautomation.entities;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import br.edu.ufcg.ccc.homeautomation.R;
import br.edu.ufcg.ccc.homeautomation.managers.RESTManager;
import br.edu.ufcg.ccc.homeautomation.managers.UserManager;
import br.edu.ufcg.ccc.homeautomation.networking.RequestsCallbackAdapter;

public class DeviceAdapter extends BaseAdapter{
	private List<Device> mDevices;
	private LayoutInflater mInflater;
	private ImageButton editDevice;
	private ImageButton buttonStatus;
	private Spinner spinTimes;
	private Spinner spinTemp;
	private Drawable on;
	private Drawable off;
	private Device mDev;
	
	private Animation rotate;
	
	private int settedTime;	
	private int settedTemp = -1;
	
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
		
		rotate = AnimationUtils.loadAnimation(view.getContext(),R.anim.rotate);
//		prefs = PreferenceManager.getDefaultSharedPreferences(view.getContext());
		
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name_device);
		final String devName = dev.getName();
		tv_name.setText(devName);
		
		final Drawable on = view.getResources().getDrawable(R.drawable.switch_icon_on);
		final Drawable off = view.getResources().getDrawable(R.drawable.switch_icon_off);
		
		TextView tv_status = (TextView) view.findViewById(R.id.tv_status_device);
		String status= "";
		try{
			int id = Integer.parseInt(dev.getStatusStringID());
			status = view.getResources().getString(id);
		}catch(Exception e) {
			status = dev.getStatusStringID();
		}
		
		
		tv_status.setText(status);
		
		final ImageButton ib_edit = (ImageButton) view.findViewById(R.id.ib_icon_edit);
		
		ib_edit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(final View v) {
				
				
				rotate.setAnimationListener(new AnimationListener() {
					
					
					public void onAnimationStart(Animation animation) {
						
					}
					
					public void onAnimationRepeat(Animation animation) {
						
					}
					
					public void onAnimationEnd(Animation animation) {
						callDialogEditDevie(v, dev);
					}
				});				
			    v.startAnimation(rotate);
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
				executeSwitchPower(v, dev, ib_switch,on,off, !dev.getStatus());
			}
		});
		
		return view;
	}
	
	private void executeSwitchPower(final View v,final Device device, final ImageButton ib_switch,
			final Drawable on, final Drawable off, boolean status){
		
		final boolean newStatus = status;
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
					device.setStatus(newStatus);
				}else{
					Toast.makeText(v.getContext(), v.getResources().getString(R.string.cannot_change_status), Toast.LENGTH_SHORT).show();
				}
				notifyDataSetChanged();
			}
		}, device);
	}

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
			spinTemp = (Spinner) dialogEditDevice.findViewById(R.id.spinner_default_temperatures);
		}
		
		on = dialogEditDevice.getContext().getResources().getDrawable(R.drawable.switch_icon_on);
		off = dialogEditDevice.getContext().getResources().getDrawable(R.drawable.switch_icon_off);
		editDevice = (ImageButton) dialogEditDevice.findViewById(R.id.button_edit_device);
		buttonStatus = (ImageButton) dialogEditDevice.findViewById(R.id.button_status_config);
		spinTimes = (Spinner) dialogEditDevice.findViewById(R.id.spinner_default_times);
		mDev = d;
		
//		Integer times[] = {0,1,5,10,15,30,60,90,120};
//		Integer temperatures[] = {17,18,19,20,21,22,23,24,25};
		
		spinTimes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				settedTime = Integer.parseInt((String) spinTimes.getSelectedItem());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
		spinTemp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> arg0, View arg1,
					int arg2, long arg3) {
				
				settedTemp = Integer.parseInt((String) spinTemp.getSelectedItem());
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0) {
				// TODO Auto-generated method stub
				
			}
		});
		
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
			public void onClick(final View v) {
				
				
				rotate.setAnimationListener(new AnimationListener() {
					
					
					public void onAnimationStart(Animation animation) {
						
					}
					
					public void onAnimationRepeat(Animation animation) {
						
					}
					
					public void onAnimationEnd(Animation animation) {
						showEditDevices(v, mDev, deviceName);
					}
				});				
			    v.startAnimation(rotate);
			}			
		});
		
		final Animation animationPress = AnimationUtils.loadAnimation(v.getContext(),R.anim.reversed_scale);
		
		buttonStatus.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				v.startAnimation(animationPress);
				executeSwitchPower(v, mDev, buttonStatus, on, off, !mDev.getStatus());
			}			
		});
		
		bt_Confirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (settedTemp != -1){
					try{
						((AirCondition)mDev).setTemperature(settedTemp);
					}catch(Exception e){
						
					}
				}
				
				if (settedTime != 0){
					mDev.setTimer(settedTime);
					executeSwitchPower(v, mDev, buttonStatus, on, off, true);
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							mDev.setStatus(false);
							notifyDataSetChanged();
						}
					}, settedTime*1000);
				}else{
					mDev.setStatus(!mDev.getStatus());
					executeSwitchPower(v, mDev, buttonStatus, on, off, !mDev.getStatus());
				}
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

}

