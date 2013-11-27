package br.edu.ufcg.ccc.homeautomation.entities;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import br.edu.ufcg.ccc.homeautomation.R;

public class DeviceAdapter extends BaseAdapter{
	private List<Device> mDevices;
	private LayoutInflater mInflater;
	
	public DeviceAdapter (Context context, List<Device> users){
		mInflater = LayoutInflater.from(context);
		mDevices = users;
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
		
		view = mInflater.inflate(R.layout.usuarios_adapter, null);
		Device user = mDevices.get(posicao);
		
		TextView name = (TextView) view.findViewById(R.id.userName);
		String nameUser = user.getName();
		name.setText(nameUser);
		return view;
	}

}

