package br.edu.ufcg.ccc.homeautomation;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import br.edu.ufcg.ccc.homeautomation.entities.Device;
import br.edu.ufcg.ccc.homeautomation.entities.DeviceAdapter;
import br.edu.ufcg.ccc.homeautomation.entities.User;
import br.edu.ufcg.ccc.homeautomation.entities.UserAdapter;

public class MainActivity extends FragmentActivity implements
	
	


	ActionBar.TabListener {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_activity);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}
	
	

	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			// Show 3 total pages.
			return 3;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.inicio).toUpperCase(l);
			case 1:
				return getString(R.string.usuarios).toUpperCase(l);
			case 2:
				return getString(R.string.dispositivos).toUpperCase(l);
			}
			return null;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		
		int deviceSelecionado = 0;
		public static final String ARG_SECTION_NUMBER = "section_number";	
		int posicaAexcluir = 0;

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			
			
			
			View rootView = inflater.inflate(R.layout.fragment_main_dummy,
					container, false);
			
			switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
			case 1:
				 rootView = inflater.inflate(R.layout.inicio,
						container, false);
				 Spinner devices = (Spinner) rootView.findViewById(R.id.spinnerDevices);
				 
				 List<String> nomes = new ArrayList<String>();
				 
				 nomes.add("Ba!");
				 nomes.add("Be!");
				 nomes.add("Bi!");
				 
				 ArrayAdapter<String> adapter = new ArrayAdapter<String>(rootView.getContext(), android.R.layout.simple_dropdown_item_1line,nomes);
				 
					final Switch sw = (Switch) rootView.findViewById(R.id.switch1);
					final TextView dummyTextView = (TextView) rootView
							.findViewById(R.id.section_label);
					sw.setOnCheckedChangeListener(new OnCheckedChangeListener() {
						
						@Override
						public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
							
							
							
							if(isChecked){
								dummyTextView.setText("On!" +deviceSelecionado);
							}else{
								dummyTextView.setText("Off!" + deviceSelecionado);
							}
							
							// TODO Auto-generated method stub
							
						}
					});
				
				 
				 devices.setAdapter(adapter);
				 
					devices.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
						 
						@Override
						public void onItemSelected(AdapterView<?> parent, View v, int posicao, long id) {
							//pega nome pela posição
							String nome = parent.getItemAtPosition(posicao).toString();
							deviceSelecionado = posicao;
							
							if(posicao == 2){
								sw.setChecked(true);
							}else{
								sw.setChecked(false);
							}
							//Colocar aqui requisição de enviar ligado/desligado para o servidor

						}
			 
						@Override
						public void onNothingSelected(AdapterView<?> parent) {
			 
						}
					});
					

				 
				 
				 break;
			case 2:
				rootView = inflater.inflate(R.layout.users,
						container, false);
				final ListView list = (ListView) rootView.findViewById(R.id.listUser);
				
				
				
				String js = "{\"name\": \"Joao\",\"user\": \"admin\",\"pass\": \"21232f297a57a5a743894a0e4a801fc3\",\"admin\": \"true\",\"house\": [],\"email\": \"joaomaravilha@gmail.com\"}";
				final List<User> lista = new ArrayList<User>();
				
				/*
				 *  Este for não será mais necessario apos a implementação das requisições
				 *  Esta ai apenas para mostrar que o updateList Funcionalidade
				 */
				
				for (int i = 0; i < 20; i++) {
					
					js  = "{\"name\": \"Joao"+ i +"\",\"user\": \"admin\",\"pass\": \"21232f297a57a5a743894a0e4a801fc3\",\"admin\": \"true\",\"house\": [],\"email\": \"joaomaravilha@gmail.com\"}";
					
					JSONObject listaj;

						try {
							listaj = new JSONObject(js);
						} catch (JSONException e) {
							listaj = new JSONObject();
							e.printStackTrace();
						}

					User us = new User(listaj, String.valueOf(i));
					lista.add(us);
					
					//lista.add(new User()
				}
				 
				PopupWindow option = new PopupWindow(rootView.getContext());
				final UserAdapter ad = new UserAdapter(rootView.getContext(), lista);
				final Dialog excluir = new Dialog(rootView.getContext());
				excluir.setContentView(R.layout.popup_layout);
				excluir.setTitle(getResources().getString(R.string.delete));
				
				/*
				 * Botoes do painel de excluir
				 */
				
				Button excluirSim = (Button) excluir.findViewById(R.id.excluirSim);
				Button excluirNao = (Button) excluir.findViewById(R.id.excluirNao);
				
				excluirSim.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						UserAdapter adp = ad;
						
						//Implementar exclusão do usuario
						//posicaoAexcluir representa o o indice a ser deletado;
						
						// Enviar requisição para deletar o usuario do servidor aqui!
						
						// Metodo para atualizar o list view com os dados do servidor!
						adp = updateUser(v, lista);
						list.setAdapter(adp);
						System.out.println("foooooooooooooi");
						excluir.dismiss();
						
						
					}
				});
				
				excluirNao.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						excluir.dismiss();
						// TODO Auto-generated method stub
						
					}
				});
				
				final AlertDialog alert;
				AlertDialog.Builder builder = new AlertDialog.Builder(rootView.getContext());
				 builder.setTitle("Titulo");
				 builder.setMessage("Pegou muleque doido");
//				 builder.setPositiveButton("Positivo", new DialogInterface.OnClickListener() {
//			            public void onClick(DialogInterface arg0, int arg1) {
//			                Toast.makeText(rootView.getContext(), "positivo=" + arg1, Toast.LENGTH_SHORT).show();
//			            }}


		

				 alert = builder.create();
				 //ccd
				 
				 
				 list.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0, View arg1,
							int position, long arg3) {
						posicaAexcluir = position;
						excluir.setTitle(getResources().getString(R.string.delete) + position);
						excluir.show();
						
					}
					 
					 
				 
				 });
				
				list.setAdapter(ad);
				break;
			case 3:
				rootView = inflater.inflate(R.layout.layout_devices,
						container, false);
				ListView listaDevices = (ListView) rootView.findViewById(R.id.listaDevices);
				final List<Device> listaDev = new ArrayList<Device>();
				for (int i = 0; i < 20; i++) {
					
					js  = "{\"name\": \"Dispositivo"+ i +"\",\"id\": \"4\",\"status\": \"true\",\"type\": \"okay\"}";
					
					
					JSONObject listaj;

						try {
							listaj = new JSONObject(js);
						} catch (JSONException e) {
							listaj = new JSONObject();
							e.printStackTrace();
						}

					Device us = new Device(listaj);
					listaDev.add(us);
					
					//lista.add(new User()
				}
				
				DeviceAdapter dAd = new DeviceAdapter(rootView.getContext(), listaDev);
				listaDevices.setAdapter(dAd);
				
				
				
				
			default:
				break;
			}
			
		
		
			return rootView;
		}
	}

	
	/*
	 * Neste metodo, A listView será atualizada.
	 * Ela sera atualizada ao invex do For, será realizada a requisição para o servidor!
	 */
	private static UserAdapter updateUser(View v,List lista){
		String js;
		List<User> usList = new ArrayList<User>();
		for (int i = 0; i < 20; i++) {
			
			js  = "{\"name\": \"JoaoNovo"+ i +"\",\"user\": \"admin\",\"pass\": \"21232f297a57a5a743894a0e4a801fc3\",\"admin\": \"true\",\"house\": [],\"email\": \"joaomaravilha@gmail.com\"}";
			
			
			JSONObject listaj;

				try {
					listaj = new JSONObject(js);
				} catch (JSONException e) {
					listaj = new JSONObject();
					e.printStackTrace();
				}

			User us = new User(listaj, String.valueOf(i));
			usList.add(us);
			
			//lista.add(new User()
		}	
		
		
		lista = usList;
		UserAdapter ad = new UserAdapter(v.getContext(), lista);
		usList = null;
		return ad;
	}
	
	private static UserAdapter updateDevice(View v,List lista){
		String js;
		List<Device> usList = new ArrayList<Device>();
		for (int i = 0; i < 20; i++) {
			
			js  = "{\"name\": \"JoaoNovo"+ i +"\",\"id\": \"4\",\"status\": \"true\",\"type\": \"okay\"}";
			
			
			JSONObject listaj;

				try {
					listaj = new JSONObject(js);
				} catch (JSONException e) {
					listaj = new JSONObject();
					e.printStackTrace();
				}

			Device us = new Device(listaj);
			usList.add(us);
			
			//lista.add(new User()
		}	
		
		
		lista = usList;
		UserAdapter ad = new UserAdapter(v.getContext(), lista);
		usList = null;
		return ad;
	}
}
