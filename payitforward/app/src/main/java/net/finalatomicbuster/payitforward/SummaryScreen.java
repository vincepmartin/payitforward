package net.finalatomicbuster.payitforward;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class SummaryScreen extends ActionBarActivity {

    String qrCode;
    String giftChoice;
    String locationCoords;
    String noteInfo;

    TextView qrCodeTextView;
    TextView giftChoiceTextView;
    TextView locationCoordsTextView;
    TextView noteInfoTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary_screen);

        //Put stuff on the screen.
        grabData();
        putDataOnScreen();
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
        qrCode = GlobalStateData.getInstance().getQRCode();
        giftChoice = GlobalStateData.getInstance().getGiftOption().toString();
        locationCoords = GlobalStateData.getInstance().getLocation();
        noteInfo = GlobalStateData.getInstance().getNotes();
    }

    void putDataOnScreen(){
        qrCodeTextView = (TextView) findViewById(R.id.textViewQR);
        giftChoiceTextView = (TextView) findViewById(R.id.textViewGiftChoice);
        locationCoordsTextView = (TextView) findViewById(R.id.textViewLocation);
        noteInfoTextView = (TextView) findViewById(R.id.textViewNote);


        qrCodeTextView.setText(qrCode);
        giftChoiceTextView.setText(giftChoice);
        locationCoordsTextView.setText(locationCoords);
        noteInfoTextView.setText(noteInfo);
    }
}
