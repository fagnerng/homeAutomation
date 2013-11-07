package br.edu.ufcg.ccc.homeautomation;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import br.edu.ufcg.ccc.homeautomation.entities.User;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        JSONObject json = new JSONObject();
        try{
		    json.put("name", "brunoffp");
		    json.put("email", "brunoffp@mail.com");
		    json.put("user", "brunoffp");
		    json.put("pass", "brunoffp");
		    json.put("house", "house_001");
		    json.put("admin", "true");
        }catch(JSONException e){
        }
        
        User u = new User(json);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
