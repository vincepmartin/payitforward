package net.finalatomicbuster.payitforward;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import com.getpebble.android.kit.PebbleKit;
import com.getpebble.android.kit.PebbleKit.PebbleDataReceiver;
import com.getpebble.android.kit.util.PebbleDictionary;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends ActionBarActivity {


    //Define the variables for our buttons and intents.
    Intent activitySelectionIntent;
    Intent activitySettingsIntent;

    //Define some PEBBLE stuff.
    private static final UUID WATCHAPP_UUID = UUID.fromString("728a5d46-18fb-4e37-bfae-803b7367decd");
    public static final String MY_PREFS_NAME = "settings";

    private Handler handler = new Handler();
    private PebbleDataReceiver appMessageReciever;


    private static final int
            KEY_BUTTON = 0,
            KEY_VIBRATE = 1,
            BUTTON_UP = 0,
            BUTTON_SELECT = 1,
            BUTTON_DOWN = 2;

    //Let us define some static doodads for our app...
            static final int OPTION_1 = 0;
            static final int OPTION_2 = 1;
            static final int OPTION_3 = 2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/comic-neue.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //TEST Global info stuff. TEST
        //GlobalStateData.getInstance().setNotes("Super Derp");
        //Log.v("Global Data Test:MainActivity", "Super Derp");


        //Setup the intent to call the activity selection screen.

        //KEEP ALIVE FOR PEBBLE
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        //Setup our login button.
        Button loginButton = (Button)findViewById(R.id.button_login);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent("com.google.zxing.client.android.SCAN");
                    intent.putExtra("SCAN_MODE", "QR_CODE_MODE"); // "PRODUCT_MODE for bar codes
                    startActivityForResult(intent, 0);
                } catch (Exception e) {
                    Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
                    Intent marketIntent = new Intent(Intent.ACTION_VIEW,marketUri);
                    startActivity(marketIntent);
                }

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

        switch (id){
            case R.id.action_settings:
                activitySettingsIntent= new Intent(this,SettingsActivity.class);
                startActivity(activitySettingsIntent);

        }

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

    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                System.out.println("The content is: " + contents);
                System.out.println("The format is: " + format);
                GlobalStateData.getInstance().setQRCode(contents);
                System.out.println(GlobalStateData.getInstance().getQRCode());
                callActivitySelection();
            } else if (resultCode == RESULT_CANCELED) {
                System.out.println("The request was cancelled");
            }
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    //PEBBLE ADDITION
    protected void onResume() {
            super.onResume();

        // Define AppMessage behavior
        if(appMessageReciever == null) {
            appMessageReciever = new PebbleDataReceiver(WATCHAPP_UUID) {
                @Override
                public void receiveData(Context context, int transactionId, PebbleDictionary data) {

                    // Always ACK
                    PebbleKit.sendAckToPebble(context, transactionId);
                    // What message was received?
                    if(data.getInteger(KEY_BUTTON) != null) {
                    // KEY_BUTTON was received, determine which button
                        final int button = data.getInteger(KEY_BUTTON).intValue();
                    // Update UI on correct thread
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                switch(button) {
                                    case OPTION_1:
                                        Log.v("Pebble", "Gift 1");
                                        GlobalStateData.getInstance().setGiftOption("1");
                                        launchPebbleAutoGifter();
                                        break;

                                    case OPTION_2:
                                        Log.v("Pebble", "Gift 2");
                                        GlobalStateData.getInstance().setGiftOption("2");
                                        launchPebbleAutoGifter();

                                        break;

                                    case OPTION_3:
                                        Log.v("Pebble", "Gift 3");
                                        GlobalStateData.getInstance().setGiftOption("3");
                                        launchPebbleAutoGifter();

                                        break;

                                    default:
                                        Toast.makeText(getApplicationContext(), "Unknown button: " + button, Toast.LENGTH_SHORT).show();
                                        break;
                                }
                            }
                        });
                    }
                }
            };
// Add AppMessage capabilities
            PebbleKit.registerReceivedDataHandler(this, appMessageReciever);
        }
    }

    void launchPebbleAutoGifter(){
        //Enable the pebble.
        GlobalStateData.getInstance().setPebbleEnabled(true);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        String provider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(provider);
        GlobalStateData.getInstance().setLocation(location.getLongitude(),location.getLatitude());

        String locationString = GlobalStateData.getInstance().getLocation();

        //Set stuff
        GlobalStateData.getInstance().setQRCode("54bba612f153b30c001cbbba");
        GlobalStateData.getInstance().setNotes("You Just helped your Favorite Person!");
        //GlobalStateData.getInstance().setLocation(location.getLongitude(), location.getLatitude());

        String qrCode = "54bba612f153b30c001cbbba";


        System.out.println("Posting");
        System.out.println("QR");
       // String qrCode = GlobalStateData.getInstance().getQRCode();
        System.out.println(qrCode);
        System.out.println("gift");
        String giftOption = GlobalStateData.getInstance().getGiftOption();
        System.out.println(giftOption);
        System.out.println("location");
//        String locationCoords = GlobalStateData.getInstance().getLocation();
        System.out.println(locationString);
        System.out.println("notes");
        String noteInfo = " ";
        System.out.println("got data");

        System.out.println(giftOption);

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://helpinghand.me/postmates/placeorder/");

        System.out.println("made client");
        // Add your data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("loc", locationString));
        nameValuePairs.add(new BasicNameValuePair("gift", giftOption));
        nameValuePairs.add(new BasicNameValuePair("paid", "paid"));
        nameValuePairs.add(new BasicNameValuePair("id", qrCode));
        nameValuePairs.add(new BasicNameValuePair("note", noteInfo));
        System.out.println("set data");


        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            System.out.println("in try");
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            System.out.println(responseString);
            GlobalStateData.getInstance().setOrderID(responseString);
            System.out.println("Posted");

            Intent pebbleIntent = new Intent(this,SummaryScreen.class);
            startActivity(pebbleIntent);


        } catch (ClientProtocolException e) {
            return;
        } catch (IOException e) {
            return;
        }
    };




}
