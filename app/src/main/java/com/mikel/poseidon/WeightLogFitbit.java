package com.mikel.poseidon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by mikel on 23/01/2017.
 */

public class WeightLogFitbit {

    //Variables in my JSON
    @SerializedName("bmi")
    @Expose
    private String bmi;

    @SerializedName("date")
    @Expose
    private String date;

    @SerializedName("logId")
    @Expose
    private String logId;

    @SerializedName("source")
    @Expose
    private String source;

    @SerializedName("time")
    @Expose
    private String time;

    @SerializedName("weight")
    @Expose
    private String weight;

    public WeightLogFitbit(){}

    //Getters and setters
    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getSource() {
        return source;
    }
    public void setSource(String source) {
        this.source = source;
    }

    public String getBmi(){
        return bmi;
    }
    public void setBmi(String bmi) {
            this.bmi = bmi;
        }
    public String getTime() {
        return time;
    }
        public void setTime(String time) {
            this.time = time;
        }
    public String getLogId() {
        return logId;
    }
    public void setLogId(String logId) {
            this.logId = logId;
    }








}
