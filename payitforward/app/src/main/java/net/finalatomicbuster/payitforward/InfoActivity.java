package net.finalatomicbuster.payitforward;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class InfoActivity extends ActionBarActivity {

    //Define the intents used.
    Intent activityLocationSelector;
    Intent activityTakePicture;

    //Stuff for the edit text boxes...
    EditText noteText;
    String noteTextString;

//    EditText qrText;
//    String qrTextString;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/comic-neue.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );



        //Define the information for our buttons.

        //Setup our picture button.
        Button pictureButton = (Button)findViewById(R.id.buttonSnapPhoto);

        //Setup our location button.
        Button locationButton = (Button)findViewById(R.id.buttonSelectLocation);

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callLocationSelector();
            }

        });

       
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info, menu);
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

    void callLocationSelector(){
        Log.v("InfoActivity:", "Call Location Selection");

        //Save the information
        noteText = (EditText) findViewById(R.id.editTextNote);
        noteTextString = noteText.getText().toString();
        Log.v("InfoActivity: noteTextString Value:",noteTextString);

//        qrText = (EditText) findViewById(R.id.editTextQR);
//        qrTextString = qrText.getText().toString();
//        Log.v("InfoActivity: qrTextString Value:",qrTextString);



//        GlobalStateData.getInstance().setQRCode(qrTextString);
        GlobalStateData.getInstance().setNotes(noteTextString);

        activityLocationSelector = new Intent(this,LocationSelectionActivity.class);
        startActivity(activityLocationSelector);
    }


    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
