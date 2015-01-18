package net.finalatomicbuster.payitforward;

import android.content.Context;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

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
import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;


public class SummaryScreen extends ActionBarActivity {

    String qrCode;
    String giftOption;
    String locationCoords;
    String note;

    String status;
    String fee;
    String courier = "Robo";

    TextView stat ;
    TextView fees ;
    TextView notes;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_screen);

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/comic-neue.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );
//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

        //Put stuff on the screen.

        stat = (TextView) findViewById(R.id.status);
        fees = (TextView) findViewById(R.id.fee);
        notes = (TextView) findViewById(R.id.notes);

        grabData();
        updateData();
//        updateScreen();
//        postData();
        //setNotification();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_summary_screen, menu);
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

    void grabData(){
        System.out.println("QR");
        qrCode = GlobalStateData.getInstance().getQRCode();
        System.out.println("gift");
        giftOption = GlobalStateData.getInstance().getGiftOption();
        System.out.println("location");
        locationCoords = GlobalStateData.getInstance().getLocation();
        System.out.println("notes");
        note = GlobalStateData.getInstance().getNotes();
    }

    void putConfirmOnScreen(){
////        qrCodeTextView = (TextView) findViewById(R.id.textViewQR);
////        giftChoiceTextView = (TextView) findViewById(R.id.textViewGiftChoice);
////        locationCoordsTextView = (TextView) findViewById(R.id.textViewLocation);
//        noteInfoTextView = (TextView) findViewById(R.id.textViewNote);
//
//
//        qrCodeTextView.setText(qrCode);
//        giftChoiceTextView.setText(giftOption);
//        locationCoordsTextView.setText(locationCoords);
//        noteInfoTextView.setText(noteInfo);
    }

//    void post(){
//
//        System.out.println("Posting");
//        System.out.println("QR");
//        qrCode = GlobalStateData.getInstance().getQRCode();
//        System.out.println("gift");
//        giftOption = GlobalStateData.getInstance().getGiftOption();
//        System.out.println("location");
//        locationCoords = GlobalStateData.getInstance().getLocation();
//        System.out.println(locationCoords);
//        System.out.println("notes");
//        noteInfo = GlobalStateData.getInstance().getNotes();
//        System.out.println("got data");
//
//        System.out.println(giftOption);
//
//        HttpClient httpclient = new DefaultHttpClient();
//        HttpPost httppost = new HttpPost("http://helpinghand.me/postmates/placeorder/");
//
//        System.out.println("made client");
//        // Add your data
//        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
//        nameValuePairs.add(new BasicNameValuePair("loc", locationCoords));
//        nameValuePairs.add(new BasicNameValuePair("gift", giftOption));
//        nameValuePairs.add(new BasicNameValuePair("paid", "paid"));
//        nameValuePairs.add(new BasicNameValuePair("id", qrCode));
//        nameValuePairs.add(new BasicNameValuePair("note", noteInfo));
//        System.out.println("set data");
//
//
//        try {
//            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
//            System.out.println("in try");
//            // Execute HTTP Post Request
//            HttpResponse response = httpclient.execute(httppost);
//            HttpEntity entity = response.getEntity();
//            String responseString = EntityUtils.toString(entity, "UTF-8");
//            System.out.println(responseString);
//            GlobalStateData.getInstance().setOrderID(responseString);
//            System.out.println("Posted");
//            return;
//
//        } catch (ClientProtocolException e) {
//            return;
//        } catch (IOException e) {
//            return;
//        }
//    };

    public void updateData(){

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://helpinghand.me/postmates/checkstatus/");


        System.out.println(GlobalStateData.getInstance().getOrderID());

        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("id",GlobalStateData.getInstance().getOrderID()));

        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            String[] responseString = EntityUtils.toString(entity, "UTF-8").split(";");
            status = responseString[0];
            System.out.println(responseString[0]);
            fee = String.format("$%.2f",Float.valueOf(responseString[1])/100 +
                    Float.valueOf(GlobalStateData.getInstance().getGiftOption()) * 5);
            System.out.println(responseString[1]);


            note = GlobalStateData.getInstance().getNotes();

            setInfo();

        } catch (ClientProtocolException e) {

        } catch (IOException e) {

        }

    }

    public void setInfo(){
        stat.setText("Status: "+status);

        fees.setText("Fee: "+fee);

        notes.setText("Notes: "+note);

    }

    public void refresh(View view){
        grabData();
        updateData();
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
