package br.edu.ufcg.ccc.homeautomation.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import br.edu.ufcg.ccc.homeautomation.AdminActivity;
import br.edu.ufcg.ccc.homeautomation.GuideRootActivity;
import br.edu.ufcg.ccc.homeautomation.LoginActivity;
import br.edu.ufcg.ccc.homeautomation.R;

import com.jayway.android.robotium.solo.Solo;

public class GuideTest extends ActivityInstrumentationTestCase2<LoginActivity> {
	private Solo solo;
	
	public GuideTest() {
		super(LoginActivity.class);

	}

	protected void setUp() throws Exception {
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
		EditText mUsernameView = (EditText) getActivity().findViewById(R.id.username);
		EditText mPasswordView = (EditText)  getActivity().findViewById(R.id.password);
		solo.enterText(mUsernameView, "fagnerng");
		solo.enterText(mPasswordView, "fagnerng");
		solo.clickOnButton("Login");
	}
	
	public void test1ShowGuide(){
		solo.assertCurrentActivity("ok", LoginActivity.class);
		
		solo.assertCurrentActivity("acticity invalid", GuideRootActivity.class);
		solo.clickOnImageButton(0);
		solo.assertCurrentActivity("acticity invalid", AdminActivity.class);

	}
	
	public void test2SwitchDevStauts(){
		solo.assertCurrentActivity("ok", LoginActivity.class);
		solo.clickOnImageButton(0);
	
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Boolean on = solo.searchText("On") && !solo.searchText("Off");
		solo.clickOnImageButton(0);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Boolean newOn = !solo.searchText("On") && solo.searchText("Off");
		if (on == newOn){
			fail();
		}
	}
	@Override
	protected void tearDown() throws Exception {
		solo.goBackToActivity("LoginActivity");
		super.tearDown();
	}

}
