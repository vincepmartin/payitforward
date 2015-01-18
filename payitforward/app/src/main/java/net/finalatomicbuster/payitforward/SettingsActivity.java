package net.finalatomicbuster.payitforward;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.stripe.android.model.Card;

import java.util.Date;


public class SettingsActivity extends ActionBarActivity {

    //Store data from the settings activity.
    String creditCardName;
    String creditCardNumber;
    String creditCardCVC;
    String creditCardDate;

    int creditCardExpirationMonth;
    int creditCardExpirationDay;

    //Store our card information with stripe.
    Card card;

    //Declare editText stuff
    EditText creditCardNumberText;
    EditText creditCardCVCText;
    EditText creditCardExpirationDateText;
    EditText creditCardNameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_settings, menu);
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

    void storeCardInfo(){

    }

    void grabInfoFromActivity(){
        //Define the EditText box stuff.
        creditCardNumberText = (EditText) findViewById(R.id.editTextCreditCardNumber);
        creditCardCVCText = (EditText) findViewById(R.id.editTextCreditCardSecurityNumber);
        creditCardExpirationDateText = (EditText) findViewById(R.id.editTextCreditCardExperationDate);
        creditCardNameText = (EditText) findViewById(R.id.editTextCreditCardName);

        //Grab the information from the text boxes.
        creditCardName = creditCardNameText.getText().toString();
        creditCardNumber = creditCardNumberText.getText().toString();
        creditCardDate = creditCardExpirationDateText.getText().toString();
        creditCardCVC = creditCardCVCText.getText().toString();

        //Now write all this stuff to our GlobalStateData.
        saveCreditData();

    }

    void saveCreditData(){

    }
}
