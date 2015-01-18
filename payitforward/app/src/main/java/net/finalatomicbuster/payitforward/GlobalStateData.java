package net.finalatomicbuster.payitforward;

import android.app.Application;
import android.util.Log;

/**
 * Created by vmartin on 1/17/15.
 */
public class GlobalStateData {
    private static GlobalStateData ourInstance = new GlobalStateData();

    public String locationOfDelivery;
    public String giftOption;
    public String QRCode;
    public String notesOnDelivery;
    public Double longitude;
    public Double latitude;
    public String orderID;

    //Stuff for billing... Yeah I know this is insecure :) It's just for the hackathon!
    public String creditCardName;
    public String creditCardNumber;
    public String creditCardCVC;
    public String creditCardDate;

    public static GlobalStateData getInstance() {
        return ourInstance;
    }


    //Setters and getters...

    //locationOfDelivery___
    public void setLocation(Double valueLongitude, Double valueLatitude){
        longitude = valueLongitude;
        latitude = valueLatitude;

        createLocationString();
    };

    public String getLocation() {
        return this.locationOfDelivery;
    }

    //giftOption___
    public void setGiftOption(String value){
        giftOption = value;
    }

    public String getGiftOption(){
        return this.giftOption;
    }

    //QRCode___
    public void setQRCode(String value){
        QRCode = value;
    }

    public String getQRCode(){
        return this.QRCode;
    }

    //notesOnDelivery___
    public String getNotes(){
        return this.notesOnDelivery;
    }

    public void setNotes(String value){
        notesOnDelivery = value;

    }

    private void createLocationString(){
        locationOfDelivery = latitude.toString()+","+longitude.toString() ;
        Log.v("Location of Delivery String:", locationOfDelivery);
    }

    public void setOrderID(String value){
        orderID = value;
    }

    public String getOrderID(){
        return this.orderID;
    }

    //The setters and getters for the billing information.


    //Setters...
    public void setCreditCardName(String value){
        creditCardName = value;
    }

    public void setCreditCardNumber(String value){
        creditCardNumber = value;
    }

    public void setCreditCardCVC(String value){
        creditCardCVC = value;
    }

    public void setCreditCardDate(String value){
        creditCardDate = value;
    }

    //Getters...
    public String getCreditCardName(){
        return this.creditCardName;
    }

    public String getCreditCardNumber(){
        return this.creditCardNumber;
    }

    public String getCreditCardCVC(){
        return this.creditCardCVC;
    }

    public String getCreditCardDate(){
        return this.creditCardDate;
    }

    public GlobalStateData() {

    }
}
