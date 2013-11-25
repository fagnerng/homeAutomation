package br.edu.ufcg.ccc.homeautomation.entities;

import java.util.List;

import br.edu.ufcg.ccc.homeautomation.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class UserAdapter extends BaseAdapter{
	
	private List<User> mUsers;
	private LayoutInflater mInflater;
	
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

	@Override
	public View getView(int posicao, View view, ViewGroup viewGroup) {
		
		view = mInflater.inflate(R.layout.usuarios_adapter, null);
		User user = mUsers.get(posicao);
		
		TextView name = (TextView) view.findViewById(R.id.userName);
		String nameUser = user.getName();
		name.setText(nameUser);
		return view;
	}

}
