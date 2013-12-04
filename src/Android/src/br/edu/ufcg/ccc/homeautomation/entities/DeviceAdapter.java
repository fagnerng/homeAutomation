package br.edu.ufcg.ccc.homeautomation.entities;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.edu.ufcg.ccc.homeautomation.R;

public class DeviceAdapter extends BaseAdapter{
	private List<Device> mDevices;
	private LayoutInflater mInflater;
	
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
		Device dev = mDevices.get(posicao);
		
		TextView tv_name = (TextView) view.findViewById(R.id.tv_name_device);
		String nameUser = dev.getName();
		tv_name.setText(nameUser);
		
		TextView tv_status = (TextView) view.findViewById(R.id.tv_status_device);
		String status = dev.getStatus();
		tv_status.setText(status);
		
//		ImageView iv_icon = (ImageView) view.findViewById(R.id.iv_icon_device);
//		iv_icon.setImageDrawable(mInflater.getContext().getResources().getDrawable((dev.getIconID())));
		return view;
	}

}

