package br.edu.ufcg.ccc.homeautomation;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import br.edu.ufcg.ccc.homeautomation.entities.Device;
import br.edu.ufcg.ccc.homeautomation.managers.UserManager;

public class DeviceEdit extends Activity {

	private ImageButton editDevice;
	private ImageButton buttonStatus;
	private NumberPicker pickerTimer;
	private Switch switchTimer;
	private Drawable on;
	private Drawable off;
	private Device mDev;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.teste_activity_device_edit);
		on = getResources().getDrawable(R.drawable.light_on);
		off = getResources().getDrawable(R.drawable.light_off);
		mDev = (Device) getIntent().getExtras().getSerializable("dev");
		editDevice = (ImageButton) findViewById(R.id.button_edit_device);
		buttonStatus = (ImageButton) findViewById(R.id.button_status_config);
		pickerTimer = (NumberPicker) findViewById(R.id.timer_picker);
		switchTimer = (Switch) findViewById(R.id.switch_timer);
		EditText editName = (EditText) findViewById(R.id.edit_device_name);
		
		if(mDev.getStatus()){
			buttonStatus.setImageDrawable(on);
		}else{
			buttonStatus.setImageDrawable(off);
		}
		
		if(!(UserManager.getInstance().getUserObject().isAdmin())){
			editDevice.setVisibility(ImageButton.INVISIBLE);
			editDevice.setClickable(false);
		}
		
		
		final TextView deviceName = (TextView) findViewById(R.id.tv_device_name);
		
		editDevice.setOnClickListener	(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showEditDevices(v,mDev,deviceName );
				
			}

			
		});

		

		
		
		deviceName.setText(mDev.getName());
		
		
		
/*		EditText etEditTaskName = (EditText) findViewById(R.id.et_edit_task);
		TextView tvStatusDev = (TextView) findViewById(R.id.tv_status_device);
		etEditTaskName.setText(mDev.getName());
		System.out.println("usuario eh root?: " + UserManager.getInstance().getUserObject().isAdmin());
		etEditTaskName.setEnabled(UserManager.getInstance().getUserObject().isAdmin());
		tvStatusDev.setText(mDev.getStatus());*/
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.device_edit, menu);
		return true;
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
	
	public Dialog onCreateDialog(Bundle savedInstanceState) {
	    AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    // Get the layout inflater
	    LayoutInflater inflater = this.getLayoutInflater();

	    // Inflate and set the layout for the dialog
	    // Pass null as the parent view because its going in the dialog layout
	    builder.setView(inflater.inflate(R.layout.dialog_edit_device, null)).setTitle("Renomear Dispositivo");     
	    return builder.create();
	}

}
