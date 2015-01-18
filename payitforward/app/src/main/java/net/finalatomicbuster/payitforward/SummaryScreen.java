package net.finalatomicbuster.payitforward;

import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class SummaryScreen extends ActionBarActivity {

    String qrCode;
    Integer giftChoice;
    String locationCoords;
    String noteInfo;

    TextView qrCodeTextView;
    TextView giftChoiceTextView;
    TextView locationCoordsTextView;
    TextView noteInfoTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        System.out.println("started");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_screen);

//        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
//        StrictMode.setThreadPolicy(policy);

        //Put stuff on the screen.
        grabData();
        //postData();
        //setNotification();
        //putConfirmOnScreen();
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
        giftChoice = GlobalStateData.getInstance().getGiftOption();
        System.out.println("location");
        locationCoords = GlobalStateData.getInstance().getLocation();
        System.out.println("notes");
        noteInfo = GlobalStateData.getInstance().getNotes();
    }

    void putConfirmOnScreen(){
        qrCodeTextView = (TextView) findViewById(R.id.textViewQR);
        giftChoiceTextView = (TextView) findViewById(R.id.textViewGiftChoice);
        locationCoordsTextView = (TextView) findViewById(R.id.textViewLocation);
        noteInfoTextView = (TextView) findViewById(R.id.textViewNote);


        qrCodeTextView.setText(qrCode);
        giftChoiceTextView.setText(giftChoice);
        locationCoordsTextView.setText(locationCoords);
        noteInfoTextView.setText(noteInfo);
    }

    void postData(){

        System.out.println("Posting");
        qrCode = GlobalStateData.getInstance().getQRCode();
        giftChoice = GlobalStateData.getInstance().getGiftOption();
        locationCoords = GlobalStateData.getInstance().getLocation();
        noteInfo = GlobalStateData.getInstance().getNotes();

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://helpinghand.me/postmates/placeorder/");

        // Add your data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("loc", locationCoords));
        nameValuePairs.add(new BasicNameValuePair("gift", giftChoice.toString()));
        nameValuePairs.add(new BasicNameValuePair("paid", "paid"));
        nameValuePairs.add(new BasicNameValuePair("id", qrCode));
        nameValuePairs.add(new BasicNameValuePair("note", noteInfo));


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
            return;

        } catch (ClientProtocolException e) {
            return;
        } catch (IOException e) {
            return;
        }
    };

}
