package net.finalatomicbuster.payitforward;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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

import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LocationSelectionActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    Boolean initialPlacementOfMarker = false;
    Marker deliveryMarker;
    LatLng tempLocation;
    String qrCode;
    String giftOption;
    String locationCoords;
    String noteInfo;
    Boolean usePebble = false;

    TextView qrCodeTextView;
    TextView giftChoiceTextView;
    TextView locationCoordsTextView;
    TextView noteInfoTextView;



    Button deliverButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_selection);
        setUpMapIfNeeded();
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                        .setDefaultFontPath("fonts/comic-neue.ttf")
                        .setFontAttrId(R.attr.fontPath)
                        .build()
        );



        //Setup some button stuff.
        Button deliverButton = (Button)findViewById(R.id.buttonDeliver);

        deliverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callActivitySummary();
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();


    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        //mMap.addMarker(new MarkerOptions().position(new LatLng(0, 0)).title("Marker"));

        //Grab my location w/ the location manager!
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
                mMap.animateCamera(cameraUpdate);
                //locationManager.removeUpdates(this);
                placeInitialMarker(latLng);

            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        //Try to set my location.
        mMap.setMyLocationEnabled(true);


    }

    void placeInitialMarker(LatLng markerLocation){
        //Take notice of the logic to stop this thing from happening over and over!
        if(initialPlacementOfMarker == false){
            deliveryMarker = mMap.addMarker(new MarkerOptions().position(markerLocation).draggable(true));
            initialPlacementOfMarker = true;
        }




    }

    void callActivitySummary(){
        //First lets save the location of our pin.
        saveLocationData();

        //Then lets setup some intent stuff so that we can call our final screen.


    }

    void saveLocationData(){
        //Temp LatLng for location to save.

        tempLocation = deliveryMarker.getPosition();
        GlobalStateData.getInstance().setLocation(tempLocation.longitude, tempLocation.latitude);

        //Jump to the final activity.
        postData();
        Intent summaryScreenIntent = new Intent(this,SummaryScreen.class);
        startActivity(summaryScreenIntent);

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    void postData(){

        System.out.println("Posting");
        System.out.println("QR");
        qrCode = GlobalStateData.getInstance().getQRCode();
        System.out.println("gift");
        giftOption = GlobalStateData.getInstance().getGiftOption();
        System.out.println("location");
        locationCoords = GlobalStateData.getInstance().getLocation();
        System.out.println(locationCoords);
        System.out.println("notes");
        noteInfo = GlobalStateData.getInstance().getNotes();
        System.out.println("got data");

        System.out.println(giftOption);

        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("http://helpinghand.me/postmates/placeorder/");

        System.out.println("made client");
        // Add your data
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair("loc", locationCoords));
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
            return;

        } catch (ClientProtocolException e) {
            return;
        } catch (IOException e) {
            return;
        }
    };

}
