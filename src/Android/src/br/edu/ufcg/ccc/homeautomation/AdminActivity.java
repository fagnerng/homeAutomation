package br.edu.ufcg.ccc.homeautomation;

import java.util.ArrayList;
import java.util.Locale;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import br.edu.ufcg.ccc.homeautomation.entities.Device;
import br.edu.ufcg.ccc.homeautomation.entities.DeviceAdapter;
import br.edu.ufcg.ccc.homeautomation.entities.User;
import br.edu.ufcg.ccc.homeautomation.entities.UserAdapter;
import br.edu.ufcg.ccc.homeautomation.listener.OnClickDeviceList;
import br.edu.ufcg.ccc.homeautomation.managers.RESTManager;
import br.edu.ufcg.ccc.homeautomation.managers.UserManager;
import br.edu.ufcg.ccc.homeautomation.networking.RequestsCallbackAdapter;

public class AdminActivity extends FragmentActivity {

	/**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
     * will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    private static User mUser;
    private static ArrayList<User> childs;
    private static ArrayList<Device> devices;
    private static ListView lv_User;
    
    public static ListView getLv_User() {
		return lv_User;
	}


	public static void updateUsers(final ListView lv, final View v){

    	RESTManager.getInstance().requestChild(new RequestsCallbackAdapter() {
            
            public void onFinishRequestChild(ArrayList<User> result) {
                childs = result;
                lv.setAdapter(new UserAdapter(v.getContext(), childs));
                for (User user : result) {
					System.out.println("USER:" + user.getName());
				}
            }
        },null);
    }
    

    
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

		
		
		// Create the adapter that will return a fragment for each of the three
        // primary sections of the app.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager)findViewById(R.id.pager);
        mViewPager.setBackgroundColor(getResources().getColor(R.color.background));
        mViewPager.setAdapter(mSectionsPagerAdapter);
        
        Intent intent = getIntent();
		int tabToOpen = intent.getIntExtra("tab", -1);
		mViewPager.setCurrentItem(tabToOpen);
      
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

   
 

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
           
        	super(fm);
           setTitleColor(getResources().getColor(R.color.white)) ;
         
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
                    return getString(R.string.tab_devices).toUpperCase(l);
                case 1:
                    return getString(R.string.tab_profile).toUpperCase(l);
                case 2:
                    return getString(R.string.tab_manage_users).toUpperCase(l);
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
        public static final String ARG_SECTION_NUMBER = "section_number";


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            View rootView = null;

            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
                case 1:
                    rootView = inflater.inflate(R.layout.activity_child, container, false);
                    mUser = UserManager.getInstance().getUserObject();
                    ListView lv_devices = (ListView)rootView.findViewById(R.id.lv_devices_child);
                    DeviceAdapter devAdapter = new DeviceAdapter(rootView.getContext(),mUser.getDevices() );
                    lv_devices.setOnItemClickListener(new OnClickDeviceList());
                    lv_devices.setAdapter(devAdapter);
                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.activity_profile_edit, container, false);
                    EditText name  = (EditText) rootView.findViewById(R.id.child_profile_name);
                    EditText email  = (EditText) rootView.findViewById(R.id.child_profile_email);
                    EditText pass  = (EditText) rootView.findViewById(R.id.child_profile_pass);
                    name.setText(mUser.getName());
                    email.setText(mUser.getEmail());
                    pass.setText("*******");
                     break;
                case 3:
                	rootView = inflater.inflate(R.layout.users, container, false);
                	
                	 lv_User = (ListView)rootView.findViewById(R.id.list_user);
                	 
                	 updateUsers(lv_User, rootView);
                	 
                	 //lv_User.setAdapter(new UserAdapter(rootView.getContext(), childs));
                	 
                	 Button newUser = (Button) rootView.findViewById(R.id.new_user_menu);
                	 final Intent i = new Intent(rootView.getContext(), RegisterActivity.class);
                	 
                	 newUser.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							
							startActivity(i);
							
						}
					});
                	 
                	break;
                	

                default:
                    break;
            }

            return rootView;

        }


    }
}
