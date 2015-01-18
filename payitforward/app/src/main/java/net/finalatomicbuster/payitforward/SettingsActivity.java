package net.finalatomicbuster.payitforward;

import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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


    //Declare editText stuff
    EditText creditCardNumberText;
    EditText creditCardCVCText;
    EditText creditCardExpirationDateText;
    EditText creditCardNameText;

    //Button stuff
    Button saveSettingsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        saveSettingsButton = (Button) findViewById(R.id.buttonSaveSettings);

        saveSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                grabInfoFromActivity();

                //Show the users it happened.
                Context context = getApplicationContext();
                CharSequence text = "Settings Saved.";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

            }
        });
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
        getCreditData();

    }

    void saveCreditData(){
        GlobalStateData.getInstance().setCreditCardNumber(creditCardName);
        GlobalStateData.getInstance().setCreditCardCVC(creditCardCVC);
        GlobalStateData.getInstance().setCreditCardDate(creditCardDate);
        GlobalStateData.getInstance().setCreditCardNumber(creditCardNumber);
    }

    void getCreditData(){
        creditCardName = GlobalStateData.getInstance().getCreditCardNumber();
        creditCardCVC = GlobalStateData.getInstance().getCreditCardCVC();
        creditCardDate = GlobalStateData.getInstance().getCreditCardDate();
        creditCardNumber = GlobalStateData.getInstance().getCreditCardNumber();

        Log.v("Testing credit card: Name", creditCardName);
        Log.v("Testing credit card: creditCardCVC", creditCardCVC);
        Log.v("Testing credit card: creditCardDate", creditCardDate);
        Log.v("Testing credit card: creditCardNumber", creditCardNumber);

    }

}
