package com.org.apnanews.article;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class State {
    @SerializedName("status")
    @Expose
    private Integer status;
    private String description;
    @SerializedName("states")
    @Expose
    private List<State_Name> states;

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

    public List<State_Name> getStates() {
        return states;
    }

    public void setStates(List<State_Name> states) {
        this.states = states;
    }

}