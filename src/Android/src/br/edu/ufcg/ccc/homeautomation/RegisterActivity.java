package br.edu.ufcg.ccc.homeautomation;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.Toast;
import br.edu.ufcg.ccc.homeautomation.entities.Device;
import br.edu.ufcg.ccc.homeautomation.managers.RESTManager;
import br.edu.ufcg.ccc.homeautomation.managers.UserManager;
import br.edu.ufcg.ccc.homeautomation.networking.RequestsCallbackAdapter;

public class RegisterActivity extends Activity {
	
	private String email;
	private String name;
	private String password;
	private String cPassword;
	private String house;
	private ArrayList<Integer> deviceIds;
	private Drawable errorIcon;
	View focusView;
	
	private void setIDs(List<CheckBox> cbList, ArrayList<Integer> idList,ArrayList<Device> devList){
		for (int i = 0; i < cbList.size(); i++) {
			if(cbList.get(i).isChecked()){
				idList.add(devList.get(i).getId());
			}
		}
	}
	
	private void createUser(String login, String password, String name, String email, String house,ArrayList<Integer> iDs){
    RESTManager.getInstance().requestChildCreate(new RequestsCallbackAdapter() {
    		boolean status;
    	
    	
            public void onFinishRequestChildCRUD (Boolean result) {
                if (result){
                    //tratamento
                	status = true;
                	Toast.makeText(getApplicationContext(),"Usuario Cadastrado", Toast.LENGTH_SHORT).show();
                }else{
                   status = false;
                }
            }
        }, login, iDs,name, email, password,house);
    

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		
		final EditText tvName = (EditText) findViewById(R.id.edNome);
		final EditText tveMail = (EditText) findViewById(R.id.edEmail);
		final EditText tvSenha = (EditText) findViewById(R.id.edSenha);
		final EditText tvConfirmSenha = (EditText) findViewById(R.id.edCsenha);
		
		final LinearLayout layout = (LinearLayout) this.findViewById(R.id.linearLayoutRegister);
		TableRow row = null;
		final List<CheckBox> cbList = new ArrayList<CheckBox>();
		final ArrayList<Device> systemDevices = UserManager.getInstance().getUserObject().getDevices();
		deviceIds = new ArrayList<Integer>();
		errorIcon = getResources().getDrawable(R.drawable.ic_launcher);
		errorIcon.setBounds(new Rect(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight()));
		for (int i = 0; i < systemDevices.size(); i++) {
			row =new TableRow(layout.getContext());
			row.setId(i);
			row.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
		    CheckBox checkBox = new CheckBox(layout.getContext());
		    checkBox.setId(i);
		    checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					// TODO Auto-generated method stub
					
				}
			});
		    
		   

		    checkBox.setText(systemDevices.get(i).getName());
		    cbList.add(checkBox);
		    row.addView(checkBox);
		    layout.addView(row);
		    
		}
		
		house = UserManager.getInstance().getUserObject().getHouse();
		
		
		
		
		
		Button newUser = (Button) findViewById(R.id.btnNewUser);
		newUser.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				setIDs(cbList,deviceIds, systemDevices);
				name = tvName.getText().toString();
				email = tveMail.getText().toString();
				password =tvSenha.getText().toString();
				cPassword = tvConfirmSenha.getText().toString();
				if(name.length() < 3){
					tvName.setError(v.getResources().getString(R.string.short_name), errorIcon);
					focusView = tvName;
					focusView.requestFocus();
				}else if (password.length() < 5){
					tvSenha.setError(v.getResources().getString(R.string.short_pass), errorIcon);
					focusView = tvSenha;
					focusView.requestFocus();
				}else if (!(password.equals(cPassword))){
					tvSenha.setError(v.getResources().getString(R.string.unmatched_pass), errorIcon);
					focusView = tvSenha;
					focusView.requestFocus();
				}else if (!(email.contains("@"))){
					tveMail.setError(v.getResources().getString(R.string.invalid_email), errorIcon);
					focusView = tveMail;
					focusView.requestFocus();
				}else if (deviceIds.size() == 0){
					cbList.get(0).setError(v.getResources().getString(R.string.no_devices));
					focusView = layout;
					focusView.requestFocus();
				}else{
					createUser(email, password, name, email, house, deviceIds);
					Intent i =  new Intent(getApplicationContext(),AdminActivity.class);
					startActivity(i);
				}
				//AdminActivity.updateUsers();
				
				
			}
		});
		
		
		
		
		
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	@Override
	public void onBackPressed() {
		Intent intent = new Intent(getApplicationContext(), AdminActivity.class);
		intent.putExtra("tab", 2);
		startActivity(intent);
	super.onBackPressed();
}

}
