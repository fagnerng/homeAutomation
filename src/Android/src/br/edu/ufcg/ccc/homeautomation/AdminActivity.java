package br.edu.ufcg.ccc.homeautomation;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import br.edu.ufcg.ccc.homeautomation.entities.User;
import br.edu.ufcg.ccc.homeautomation.managers.RESTManager;
import br.edu.ufcg.ccc.homeautomation.networking.RequestsCallbackAdapter;

public class AdminActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_admin);
		
		/*		
		System.out.println("INICIO TESTE REQUEST CHILD");

		
		RESTManager.getInstance().requestChild(new RequestsCallbackAdapter() {
			
			@Override
			public void onFinishRequestChild(ArrayList<User> result) {
				System.out.println("result length: " + result.size());
				for (int i = 0; i < result.size(); i++){
					System.out.println(result.get(i));
				}
			}
		}, null);
		
		System.out.println("FINAL TESTE REQUEST CHILD");
		
		
		
		System.out.println("INICIO TESTE REQUEST EDIT");
		
		RESTManager.getInstance().requestEdit(new RequestsCallbackAdapter() {
			
			@Override
			public void onFinishRequestEdit(Boolean result) {
				if (result) {
					System.out.println("Editado com sucesso");
				}else{
					System.out.println("Nao foi edidato");
				}
			}
		}, null, "email@gmail.com", null, -1, -1);
		
		System.out.println("FINAL TESTE REQUEST EDIT");
		
		
		System.out.println("INICIO TESTE REQUEST CHILDCreate");

		
		RESTManager.getInstance().requestChildCreate(new RequestsCallbackAdapter() {
			
			@Override
			public void onFinishRequestChildCRUD (Boolean result) {
				if (result){
					System.out.println("deu certo a bagaça");
				}else{
					System.out.println("nao deu certo");
				}
			}
		}, "diguego", new ArrayList<Integer>(),"Diego Tavares", "email@mailsdkasj.com", "1234","house_000");
		
		System.out.println("FINAL TESTE REQUEST CHILDCreate");		

		
		System.out.println("INICIO TESTE REQUEST CHILDDelete");

		
		RESTManager.getInstance().requestChildDelete(new RequestsCallbackAdapter() {
			
			@Override
			public void onFinishRequestChildCRUD (Boolean result) {
				if (result){
					System.out.println("deu certo a bagaça");
				}else{
					System.out.println("nao deu certo");
				}
			}
		}, "diguego");
		
		
		System.out.println("FINAL TESTE REQUEST CHILDDelete");	
		
		
		System.out.println("INICIO TESTE REQUEST CHILDUpdate");
		
		RESTManager.getInstance().requestChildUpdate(new RequestsCallbackAdapter() {
			
			@Override
			public void onFinishRequestChildCRUD (Boolean result) {
				if (result){
					System.out.println("deu certo a bagaça");
				}else{
					System.out.println("nao deu certo");
				}
			}
		}, "andersongfs",new ArrayList<Integer>());
		
		
		System.out.println("FINAL TESTE REQUEST CHILDUpdate");
		*/	
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.admin, menu);
		return true;
	}

}
