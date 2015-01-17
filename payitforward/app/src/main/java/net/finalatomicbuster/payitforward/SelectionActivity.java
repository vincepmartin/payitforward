package net.finalatomicbuster.payitforward;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;


public class SelectionActivity extends ActionBarActivity {

    //Let us now set some vars to define our donation options.
    Boolean donationOption1 = true;
    Boolean donationOption2 = false;
    Boolean donationOption3 = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selection);
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
        switch(view.getId()) {
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

    public void setDonation(int selectedDonation){

        // I know this is being a bit crazy... But yeah I made a function just to do this...

        switch(selectedDonation){
            case 1:
                donationOption1 = true;
                donationOption2 = false;
                donationOption3 = false;
                Log.v("SelectionActivity:","true, false, false");
                break;

            case 2:
                donationOption1 = false;
                donationOption2 = true;
                donationOption3 = false;
                Log.v("SelectionActivity:","false, true, false");
                break;

            case 3:
                donationOption1 = false;
                donationOption2 = false;
                donationOption3 = true;
                Log.v("SelectionActivity:","false, false, true");
                break;

        }

    }
}
