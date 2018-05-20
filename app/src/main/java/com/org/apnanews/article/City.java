package com.org.apnanews.article;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by deepak on 5/16/2017.
 */

public class City {
    @SerializedName("status")
    @Expose
    private Integer status;
    private String description;
    @SerializedName("cities")
    @Expose
    private List<City_Name> cities;

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<City_Name> getCities() {
        return cities;
    }

    public void setCities(List<City_Name> cities) {
        this.cities = cities;
    }

}