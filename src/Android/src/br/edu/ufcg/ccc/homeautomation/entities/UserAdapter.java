package br.edu.ufcg.ccc.homeautomation.entities;

import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;
import br.edu.ufcg.ccc.homeautomation.AdminActivity;
import br.edu.ufcg.ccc.homeautomation.R;
import br.edu.ufcg.ccc.homeautomation.managers.RESTManager;
import br.edu.ufcg.ccc.homeautomation.managers.UserManager;
import br.edu.ufcg.ccc.homeautomation.networking.RequestsCallbackAdapter;

public class UserAdapter extends BaseAdapter{
	
	private Animation rotate;
	private List<User> mUsers;
	private LayoutInflater mInflater;
	private ImageButton buttonEdit;
	private ImageButton buttonDelete;
	
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
	
	private void deleteUser(User user,final View v){
		 RESTManager.getInstance().requestChildDelete(new RequestsCallbackAdapter() {
	            
	            @Override
	            public void onFinishRequestChildCRUD (Boolean result) {
	                if (result){
	                	Toast.makeText(v.getContext(),v.getResources().getString(R.string.user_delete), Toast.LENGTH_SHORT).show();
	                }else{
	                }
	            }
	        }, user.getUser());

		
		AdminActivity.updateUsers(AdminActivity.getLv_User(),v);
	}
	
	private void callDialogDelete(View v, final User u){
		final Dialog excluir = new Dialog(v.getContext());
		excluir.setContentView(R.layout.popup_layout);
		excluir.setTitle(v.getResources().getString(R.string.action_delete));
		Button excluirSim = (Button) excluir.findViewById(R.id.excluirSim);
		Button excluirNao = (Button) excluir.findViewById(R.id.excluirNao);
		excluir.show();
		rotate.reset();
		excluirNao.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				excluir.dismiss();
				
			}
		});
		
		excluirSim.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				deleteUser(u, v);
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
                }else{
                }
            }
        }, user.getUser(),iDevices);

		
		
		//Chamar funcao de atializarDevices no server
		
		
		
	}
	
	@SuppressWarnings("deprecation")
	private void callDialogDevices(View v,final User user){
		final Dialog devicesDialog = new Dialog(v.getContext(),R.style.myCoolDialog);
		final ArrayList<Device> systemDevices = UserManager.getInstance().getUserObject().getDevices();
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
		rotate.reset();
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
		
		buttonEdit = (ImageButton) view.findViewById(R.id.button_edit);
		buttonDelete = (ImageButton) view.findViewById(R.id.button_remove);
		 rotate = AnimationUtils.loadAnimation(view.getContext(),R.anim.rotate);
		 
		 
		 //final View v1= view.findViewById(R.id.button_edit);
		 buttonEdit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(final View v) {
				rotate.setAnimationListener(new AnimationListener() {
					
					
					public void onAnimationStart(Animation animation) {
						
					}
					
					public void onAnimationRepeat(Animation animation) {
						
					}
					
					public void onAnimationEnd(Animation animation) {
						callDialogDevices(v, user);
						
					}
				});

			        v.startAnimation(rotate);

				
			}
		});
		
		
		//final View v2= view.findViewById(R.id.button_remove);
		buttonDelete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(final View v) {
				rotate.setAnimationListener(new AnimationListener() {
					
					
					public void onAnimationStart(Animation animation) {
						}
					
					public void onAnimationRepeat(Animation animation) {
						}
					
					public void onAnimationEnd(Animation animation) {
						callDialogDelete(v,user);
						
					}
				});

			        v.startAnimation(rotate);

				
				
			}
		});
		
		return view;
	}

}
