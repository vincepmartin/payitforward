package net.finalatomicbuster.payitforward;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class SelectionActivity extends ActionBarActivity {

    //Let us now set some vars to define our donation options.
    Boolean donationOption1 = true;
    Boolean donationOption2 = false;
    Boolean donationOption3 = false;
    Boolean pebbleEnabled = false;
    Integer donationOptionChosen = 1;
    private LocationManager locationManager;
    private Criteria criteria;
    private String provider;
    //Define the intents used.
    Intent activityLocationSelector;
    Intent activityTakePicture;

    //Stuff for the edit text boxes...
    EditText noteText;
    String noteTextString;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/comic-neue.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        GlobalStateData.getInstance().setGiftOption("1");
        //Handle our button to donate.
        Button locationButton = (Button)findViewById(R.id.buttonSelectLocation);

        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callLocationSelector();
            }

        });


        //Global info stuff.
        //Log.v("Global Data Test:SelectionActivity", GlobalStateData.getInstance().getNotes());

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);

        provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);

        Log.v(String.valueOf(location.getLatitude()),"asfd");
        //System.out.println(String.valueOf(location.getLatitude()) + "," + String.valueOf(location.getLongitude()));


        int fee = getPostmatesQuote(
                String.valueOf(location.getLatitude()) + ","
                        + String.valueOf(location.getLongitude()));

        System.out.println(fee);

        float cost = (float) fee / 100;

        RadioButton low = (RadioButton) findViewById(R.id.radio_donation1);
        RadioButton mid = (RadioButton) findViewById(R.id.radio_donation2);
        RadioButton high = (RadioButton) findViewById(R.id.radio_donation3);

        low.setText(String.format("Basic: $%.2f", (cost + 5.0)));
        mid.setText(String.format("Generous: $%.2f", (cost + 10.0)));
        high.setText(String.format("Philanthropist: $%.2f", (cost + 15.0)));

        //PEBBLE HANDLE!!!!
        pebbleEnabled= GlobalStateData.getInstance().getPebbleEnabled();

        if(pebbleEnabled)
        {
            //Lets set some default settings.
            GlobalStateData.getInstance().setQRCode("54bba612f153b30c001cbbba");
            GlobalStateData.getInstance().setNotes("You Just helped your Favorite Person!");
            GlobalStateData.getInstance().setLocation(location.getLongitude(), location.getLatitude());


            //Intent to summary.
            Intent summaryIntent = new Intent(this, SummaryScreen.class);
            startActivity(summaryIntent);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_selection, menu);
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

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch (view.getId()) {
            case R.id.radio_donation1:
                if (checked)
                    //donation 1
                    setDonation(1);
                break;
            case R.id.radio_donation2:
                if (checked)
                    //donation 2
                    setDonation(2);
                break;

            case R.id.radio_donation3:
                if (checked)
                    //donation 3
                    setDonation(3);
                break;
        }
    }

    public void setDonation(int selectedDonation) {

        // I know this is being a bit crazy... But yeah I made a function just to do this...

        switch (selectedDonation) {
            case 1:
                donationOption1 = true;
                donationOption2 = false;
                donationOption3 = false;
                donationOptionChosen = 1;
                Log.v("SelectionActivity:", "true, false, false");
                break;

            case 2:
                donationOption1 = false;
                donationOption2 = true;
                donationOption3 = false;
                donationOptionChosen = 2;
                Log.v("SelectionActivity:", "false, true, false");
                break;

            case 3:
                donationOption1 = false;
                donationOption2 = false;
                donationOption3 = true;
                donationOptionChosen = 3;
                Log.v("SelectionActivity:", "false, false, true");
                break;

        }

        //Set the selected item into the GlobalStateData store.
        GlobalStateData.getInstance().setGiftOption(String.valueOf(donationOptionChosen));
    }

    void callInfoActivity() {

        //Define the intent that will call the InfoActivity.
        Intent infoIntent = new Intent(this, InfoActivity.class);
        startActivity(infoIntent);

    }

    int getPostmatesQuote(String location) {
        System.out.println(location);
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://helpinghand.me/postmates/getquote/");

        // Add your data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("loc", location));

        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            System.out.println("in try");
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            return Integer.parseInt(responseString);

        } catch (ClientProtocolException e) {
            return -1;
        } catch (IOException e) {
            return -1;
        }
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
        System.out.println("NOTES!!!");
        System.out.println(GlobalStateData.getInstance().getNotes());

        activityLocationSelector = new Intent(this,LocationSelectionActivity.class);
        startActivity(activityLocationSelector);
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

}
