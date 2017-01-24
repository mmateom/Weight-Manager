package com.mikel.poseidon;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mikel on 23/01/2017.
 */

public class WeightList {

    @SerializedName("weight")
    @Expose

    private List<WeightLogFitbit> weight;

    public WeightList(){

    }

    public List<WeightLogFitbit> getWeight() {
        return weight;
    }


    public void setWeight(List<WeightLogFitbit> weight) {
        this.weight = weight;
    }


    /*private WeightLogFitbit[] weight;

    public WeightLogFitbit[] getWeight(){

        return weight;
    }

    public void setWeight (WeightLogFitbit[] weight){

        this.weight = weight;
    }*/
}
