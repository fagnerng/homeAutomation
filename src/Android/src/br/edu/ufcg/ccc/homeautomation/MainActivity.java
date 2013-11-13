package br.edu.ufcg.ccc.homeautomation;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;
import br.edu.ufcg.ccc.homeautomation.entities.User;
import br.edu.ufcg.ccc.homeautomation.networking.RESTManager;
import br.edu.ufcg.ccc.homeautomation.networking.RequestsCallbackAdapter;

public class MainActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        RESTManager.getInstance().requestUser(
        		new RequestsCallbackAdapter() {
    				
        			@Override
        			public void onFinishRequestUser(User userResult) {
        				// Exemplo de como receber o usuario do servidor
        				
        				TextView tvName = (TextView) findViewById(R.id.name);
        				tvName.setText(userResult.getName());
        				
        				TextView tvEmail = (TextView) findViewById(R.id.email);
        				tvEmail.setText(userResult.getEmail());
        				
        				TextView tvUser = (TextView) findViewById(R.id.user);
        				tvUser.setText(userResult.getUser());	
        				
        				TextView tvHouse = (TextView) findViewById(R.id.house);
        				tvHouse.setText(userResult.getHouse());
        			}
    			}
        		, "brunoffp", "brunoffp");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
