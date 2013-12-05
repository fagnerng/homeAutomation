package br.edu.ufcg.ccc.homeautomation;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
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
	private String house;
	private ArrayList<Integer> deviceIds;
	
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
		EditText tvConfirmSenha = (EditText) findViewById(R.id.edCsenha);
		
		final LinearLayout layout = (LinearLayout) this.findViewById(R.id.linearLayoutRegister);
		TableRow row = null;
		final List<CheckBox> cbList = new ArrayList<CheckBox>();
		final ArrayList<Device> systemDevices = UserManager.getInstance().getUserObject().getDevices();
		deviceIds = new ArrayList<Integer>();
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
				createUser(email, password, name, email, house, deviceIds);
				Intent i =  new Intent(getApplicationContext(),AdminActivity.class);
				startActivity(i);
				
			}
		});
		
		
		
		
		
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
