package com.org.apnanews.article;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by deepak on 5/16/2017.
 */

public class City_Name {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("state_id")
    @Expose
    private String stateId;
    @SerializedName("creaet_at")
    @Expose
    private String creaetAt;
    @SerializedName("updated_at")
    @Expose
    private String updatedAt;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateId() {
        return stateId;
    }

    public void setStateId(String stateId) {
        this.stateId = stateId;
    }

    public String getCreaetAt() {
        return creaetAt;
    }

    public void setCreaetAt(String creaetAt) {
        this.creaetAt = creaetAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

}