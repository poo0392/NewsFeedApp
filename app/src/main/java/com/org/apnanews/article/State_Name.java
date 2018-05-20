package com.org.apnanews.article;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by zafarhussain on 20/08/17.
 */

public class State_Name {
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("state_name")
    @Expose
    private String state_name;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getStateName() {
        return state_name;
    }

    public void setStateName(String state_name) {
        this.state_name = state_name;
    }

}