package com.org.apnanews.register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by deepak on 6/2/2017.
 */

public class LoginRegisterResponse {

    @SerializedName("status")
    @Expose
    private Integer status;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("member")
    @Expose
    private RegisterMember member;

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

    public RegisterMember getMember() {
        return member;
    }

    public void setMember(RegisterMember member) {
        this.member = member;
    }
}
