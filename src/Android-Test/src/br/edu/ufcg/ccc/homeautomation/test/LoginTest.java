package br.edu.ufcg.ccc.homeautomation.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import br.edu.ufcg.ccc.homeautomation.GuideActivity;
import br.edu.ufcg.ccc.homeautomation.GuideRootActivity;
import br.edu.ufcg.ccc.homeautomation.LoginActivity;
import br.edu.ufcg.ccc.homeautomation.R;

import com.jayway.android.robotium.solo.Solo;

public class LoginTest extends ActivityInstrumentationTestCase2<LoginActivity> {
	
	public LoginTest() {
		super(LoginActivity.class);
		// TODO Auto-generated constructor stub
	}

	private Solo solo;

	
	@Override
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		solo = new Solo(getInstrumentation(), getActivity());
	}
	

	
	public void test1InvalidPass(){
		solo.assertCurrentActivity("ok", LoginActivity.class);
		EditText mUsernameView = (EditText) getActivity().findViewById(R.id.username);
		EditText mPasswordView = (EditText)  getActivity().findViewById(R.id.password);
		solo.enterText(mUsernameView, "fagnerng");
		solo.enterText(mPasswordView, "senhaInvalida");
		solo.clickOnButton("Login");
		solo.assertCurrentActivity("acticity invalid", LoginActivity.class);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		solo.goBackToActivity("LoginActivity");
	}
	public void test2InvalidUser(){
		solo.assertCurrentActivity("ok", LoginActivity.class);
		EditText mUsernameView = (EditText) getActivity().findViewById(R.id.username);
		EditText mPasswordView = (EditText)  getActivity().findViewById(R.id.password);
		solo.enterText(mUsernameView, "usuarioInvalido");
		solo.enterText(mPasswordView, "senhaInvalida");
		solo.clickOnButton("Login");
		solo.assertCurrentActivity("acticity invalid", LoginActivity.class);	
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		solo.goBackToActivity("LoginActivity");
	}

	public void test3ValidLoginRoot(){
		solo.assertCurrentActivity("ok", LoginActivity.class);
		EditText mUsernameView = (EditText) getActivity().findViewById(R.id.username);
		EditText mPasswordView = (EditText)  getActivity().findViewById(R.id.password);
		solo.enterText(mUsernameView, "fagnerng");
		solo.enterText(mPasswordView, "fagnerng");
		solo.clickOnButton("Login");
		solo.assertCurrentActivity("acticity invalid", GuideRootActivity.class);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		solo.goBackToActivity("LoginActivity");

	
	}
	public void test4ValidLoginChild(){
		solo.assertCurrentActivity("ok", LoginActivity.class);
		EditText mUsernameView = (EditText) getActivity().findViewById(R.id.username);
		EditText mPasswordView = (EditText)  getActivity().findViewById(R.id.password);
		solo.enterText(mUsernameView, "andersongfs@gmail.com");
		solo.enterText(mPasswordView, "123456");
		solo.clickOnButton("Login");
		solo.assertCurrentActivity(getActivity().getLocalClassName(), GuideActivity.class);
		
		
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		solo.goBackToActivity("LoginActivity");
	
	}
}
