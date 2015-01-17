package net.finalatomicbuster.payitforward;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.util.Log;

public class MainActivity extends ActionBarActivity {


    //Define the variables for our buttons and intents.
    Intent activitySelectionIntent;
    Intent activityLocationSelectionIntent;




    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TEST Global info stuff. TEST
        //GlobalStateData.getInstance().setNotes("Super Derp");
        //Log.v("Global Data Test:MainActivity", "Super Derp");


        //Setup the intent to call the activity selection screen.


        //Setup our login button.
        Button loginButton = (Button)findViewById(R.id.button_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callActivitySelection();
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void callActivitySelection(){
        Log.v("MainActivity","Call Activity Selection");
        activitySelectionIntent = new Intent(this,SelectionActivity.class);
        startActivity(activitySelectionIntent);

    }


}
