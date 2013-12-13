package br.edu.ufcg.ccc.homeautomation;

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
import android.widget.Toast;
import br.edu.ufcg.ccc.homeautomation.entities.DeviceAdapter;
import br.edu.ufcg.ccc.homeautomation.entities.User;
import br.edu.ufcg.ccc.homeautomation.managers.RESTManager;
import br.edu.ufcg.ccc.homeautomation.managers.UserManager;
import br.edu.ufcg.ccc.homeautomation.networking.RequestsCallbackAdapter;

public class ChildActivity extends FragmentActivity {

	private static User mUser;
   // private static ArrayList<User> childs;
  //  private static ArrayList<Device> devices;
   // private static ListView lv_User;
    private static View focusView;

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
            return 2;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.tab_devices).toUpperCase(l);
                case 1:
                    return getString(R.string.tab_profile).toUpperCase(l);
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
            mUser = UserManager.getInstance().getUserObject();

            switch (getArguments().getInt(ARG_SECTION_NUMBER)) {
            case 1:
                rootView = inflater.inflate(R.layout.activity_child, container, false);
                
                ListView lv_devices = (ListView)rootView.findViewById(R.id.lv_devices_child);
                DeviceAdapter devAdapter = new DeviceAdapter(rootView.getContext(),mUser.getDevices() );
                lv_devices.setAdapter(devAdapter);
                break;
            case 2:
                rootView = inflater.inflate(R.layout.activity_profile_edit, container, false);
                final EditText name  = (EditText) rootView.findViewById(R.id.child_profile_name);
                final EditText email  = (EditText) rootView.findViewById(R.id.child_profile_email);
                final EditText pass  = (EditText) rootView.findViewById(R.id.child_profile_pass);
                final EditText cPass  = (EditText) rootView.findViewById(R.id.child_profile_pass_confirm);
                name.setText(mUser.getName());
                email.setText(mUser.getEmail());
                final Button tvSave = (Button) rootView.findViewById(R.id.tv_save);
                tvSave.setOnClickListener(new View.OnClickListener() {
                	
					@Override
					public void onClick(final View v) {
						 String textPass = pass.getText().toString();
						 final String textName = name.getText().toString();
						 final String textMail = email.getText().toString();
						 String textCpass = cPass.getText().toString();
						 
						 if(textName.length() < 3 ){
							 name.setError(v.getResources().getString(R.string.short_name));
							 focusView = name;
							 focusView.requestFocus();
						 }else if (textPass.length() < 4){
								pass.setError(v.getResources().getString(R.string.short_pass));
								focusView = pass;
								focusView.requestFocus();
						}else if (!(textPass.equals(textCpass))){
							pass.setError(v.getResources().getString(R.string.unmatched_pass));
							focusView = pass;
							focusView.requestFocus();
						}else if (!(textMail.contains("@") && textMail.contains("."))){
							pass.setError(v.getResources().getString(R.string.unmatched_pass));
							focusView = email;
							focusView.requestFocus();
						}else{
						RESTManager.getInstance().requestEdit(new RequestsCallbackAdapter() {
					            
					            @Override
					            public void onFinishRequestEdit(Boolean result) {
					                if (result) {
					                	UserManager.getInstance().getUserObject().setName(textName);
					                	UserManager.getInstance().getUserObject().setEmail(textMail);
					                	mUser = UserManager.getInstance().getUserObject();
					                	
					                	Toast.makeText(v.getContext(),v.getResources().getString(R.string.user_edited), Toast.LENGTH_SHORT).show();
					                }else{
					                	Toast.makeText(v.getContext(),v.getResources().getString(R.string.user_edited_fail), Toast.LENGTH_SHORT).show();
					                }
					            }
					        }, textName, textMail, pass.getText().toString(), -1, -1);
						}				
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
