
package com.mikel.poseidon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BodyWeight {

    @SerializedName("dateTime")
    @Expose
    private String dateTime;
    @SerializedName("value")
    @Expose
    private String value;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

}
