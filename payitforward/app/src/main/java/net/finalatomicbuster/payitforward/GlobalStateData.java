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
        locationOfDelivery = longitude.toString() + "," + latitude.toString();
        Log.v("Location of Delivery String:", locationOfDelivery);
    }

    public void setOrderID(String value){
        orderID = value;
    }

    public String getOrderID(){
        return this.orderID;
    }

    private GlobalStateData() {

    }
}
