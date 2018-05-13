package com.org.apnanews.article;

import java.util.List;

public class State {

    private Integer status;
    private String description;
    private List<State_Name> states = null;

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