package team.project.tripmanager.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Expense implements Serializable {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("price")
    @Expose
    private Double price;
    @SerializedName("cash")
    @Expose
    private int cash;
    @SerializedName("purchase_date")
    @Expose
    private String purchase_date;

    public String getName() {
        return name;
    }

    public Double getPrice() {
        return price;
    }

    public int getCash() {
        return cash;
    }

    public String getPurchase_date() {
        return purchase_date;
    }
}
