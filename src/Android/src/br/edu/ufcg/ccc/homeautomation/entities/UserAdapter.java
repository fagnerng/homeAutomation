package br.edu.ufcg.ccc.homeautomation.entities;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import br.edu.ufcg.ccc.homeautomation.R;
import br.edu.ufcg.ccc.homeautomation.managers.UserManager;

public class UserAdapter extends BaseAdapter{
	
	private List<User> mUsers;
	private LayoutInflater mInflater;
	private Button buttonEdit;
	private Button buttonDelete;
	
	public UserAdapter (Context context, List<User> users){
		mInflater = LayoutInflater.from(context);
		mUsers = users;
		mUsers.add(UserManager.getInstance().getUserObject());}

	
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

	@Override
	public View getView(int posicao, View view, ViewGroup viewGroup) {
		
		view = mInflater.inflate(R.layout.usuarios_adapter, null);
		final User user = mUsers.get(posicao);
		TextView name = (TextView) view.findViewById(R.id.tv_name_user);
		String nameUser = user.getName();
		name.setText(nameUser);
		
		
		
		buttonEdit = (Button) view.findViewById(R.id.button_edit);
		
		buttonEdit.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				System.out.println(user.getName());
				
			}
		});
		
		buttonDelete = (Button) view.findViewById(R.id.button_remove);
		return view;
	}

}
