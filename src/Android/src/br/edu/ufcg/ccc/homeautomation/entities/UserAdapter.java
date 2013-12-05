package br.edu.ufcg.ccc.homeautomation.entities;

import java.util.ArrayList;
import java.util.List;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TableRow.LayoutParams;
import br.edu.ufcg.ccc.homeautomation.R;
import br.edu.ufcg.ccc.homeautomation.managers.RESTManager;
import br.edu.ufcg.ccc.homeautomation.managers.UserManager;
import br.edu.ufcg.ccc.homeautomation.networking.RequestsCallbackAdapter;
import br.edu.ufcg.ccc.homeautomation.AdminActivity;

public class UserAdapter extends BaseAdapter{
	
	private List<User> mUsers;
	private LayoutInflater mInflater;
	private Button buttonEdit;
	private Button buttonDelete;
	
	public UserAdapter (Context context, List<User> users){
		mInflater = LayoutInflater.from(context);
		mUsers = users;
		}

	
	@Override
	public int getCount() {
		return mUsers.size();
	}

	@Override
	public Object getItem(int index) {
		return mUsers.get(index);
	}

	@Override
	public long getItemId(int index) {
		return index;
	}
	
	private void deleteUser(User user){
		AdminActivity.updateUsers();
	}
	
	private void callDialogDelete(View v, final User u){
		final Dialog excluir = new Dialog(v.getContext());
		excluir.setContentView(R.layout.popup_layout);
		excluir.setTitle(v.getResources().getString(R.string.action_delete));
		Button excluirSim = (Button) excluir.findViewById(R.id.excluirSim);
		Button excluirNao = (Button) excluir.findViewById(R.id.excluirNao);
		excluir.show();
		excluirNao.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				excluir.dismiss();
				
			}
		});
		
		excluirSim.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				deleteUser(u);
				excluir.dismiss();
				
			}
		});
		
		
		
	}
	
	private void updateDevices(ArrayList<Device> systemDevices,
			ArrayList<Device> userDevices, List<CheckBox> cbList, User user) {
		user.getDevices().clear();
		ArrayList<Integer> iDevices = new ArrayList<Integer>();
		for (int i = 0; i < cbList.size(); i++) {
			if(cbList.get(i).isChecked()){
				user.getDevices().add(systemDevices.get(i));
				iDevices.add(systemDevices.get(i).getId());
			}
		}
		
		RESTManager.getInstance().requestChildUpdate(new     RequestsCallbackAdapter() {
            
            @Override
            public void onFinishRequestChildCRUD (Boolean result) {
                if (result){
                    System.out.println("deu certo a bagaça");
                }else{
                    System.out.println("nao deu certo");
                }
            }
        }, user.getUser(),iDevices);
		
        

		
		
		//Chamar funcao de atializarDevices no server
		
		
		
	}
	
	private void callDialogDevices(View v,final User user,User rootUser){
		final Dialog devicesDialog = new Dialog(v.getContext(),R.style.myCoolDialog);
		final ArrayList<Device> systemDevices = rootUser.getDevices();
		final ArrayList<Device> userDevices = user.getDevices();
		
		
		
		devicesDialog.setContentView(R.layout.dialog_devices);
		final LinearLayout layout = (LinearLayout) devicesDialog.findViewById(R.id.dialogDevicesLayout);
		devicesDialog.setTitle(devicesDialog.getContext().getResources().getString(R.string.manage_devices) + " " + user.getName());
		
		TableRow row = null;
		final List<CheckBox> cbList = new ArrayList<CheckBox>();
		for (int j = 0; j < systemDevices.size(); j++) {
			row =new TableRow(devicesDialog.getContext());
			row.setId(j);
			row.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.WRAP_CONTENT));
			CheckBox checkBox = new CheckBox(devicesDialog.getContext());
			checkBox.setId(j);
			checkBox.setText(systemDevices.get(j).getName());
			checkBox.setChecked(userDevices.contains(systemDevices.get(j)));
			cbList.add(checkBox);
			row.addView(checkBox);
			layout.addView(row); 
		}
		Button confirm = (Button) devicesDialog.findViewById(R.id.confirm);
		
		confirm.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				updateDevices(systemDevices,userDevices,cbList, user);
				devicesDialog.dismiss();
				
			}

		
		});
		
		devicesDialog.show();
		
	}

	@Override
	public View getView(int posicao, View view, ViewGroup viewGroup) {
		
		view = mInflater.inflate(R.layout.usuarios_adapter, null);
		final User user = mUsers.get(posicao);
		TextView name = (TextView) view.findViewById(R.id.tv_name_user);
		String nameUser = user.getName();
		name.setText(nameUser);
		
		
		
		buttonEdit = (Button) view.findViewById(R.id.button_edit);
		buttonDelete = (Button) view.findViewById(R.id.button_remove);
		
		buttonEdit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				callDialogDevices(v, user, UserManager.getInstance().getUserObject());
				
			}
		});
		
		buttonDelete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				callDialogDelete(v,user);
				
			}
		});
		

		
		
		return view;
	}

}
