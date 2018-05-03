
package com.main.training.NewOrder;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {

    @SerializedName("dataorder")
    @Expose
    private List<Dataorder> dataorder = null;

    /**
     * No args constructor for use in serialization
     * 
     */
    public Data() {
    }

    /**
     * 
     * @param dataorder
     */
    public Data(List<Dataorder> dataorder) {
        super();
        this.dataorder = dataorder;
    }

    public List<Dataorder> getDataorder() {
        return dataorder;
    }

    public void setDataorder(List<Dataorder> dataorder) {
        this.dataorder = dataorder;
    }

}
