package net.finalatomicbuster.payitforward;

import android.app.Application;

/**
 * Created by vmartin on 1/17/15.
 */
public class GlobalStateData {
    private static GlobalStateData ourInstance = new GlobalStateData();

    public String locationOfDelivery;
    public Integer giftOption;
    public String QRCode;
    public String notesOnDelivery;


    public static GlobalStateData getInstance() {
        return ourInstance;
    }


    //Setters and getters...

    //locationOfDelivery___
    public void setLocation(String value){
        locationOfDelivery = value;
    };

    public String getLocation() {
        return this.locationOfDelivery;
    }

    //giftOption___
    public void setGiftOption(Integer value){
        giftOption = value;
    }

    public Integer getGiftOption(){
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


    private GlobalStateData() {

    }
}
