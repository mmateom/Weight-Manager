
package com.mikel.poseidon;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class WeightArray {

    @SerializedName("body-weight")
    @Expose
    private List<BodyWeight> bodyWeight = new ArrayList<>();

    public List<BodyWeight> getBodyWeight() {
        return bodyWeight;
    }

    public void setBodyWeight(List<BodyWeight> bodyWeight) {
        this.bodyWeight = bodyWeight;
    }

}
