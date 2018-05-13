package com.org.apnanews.register;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by deepak on 6/2/2017.
 */

public class RegisterMember {


    @SerializedName("first_name")
    @Expose
    private String first_name;

    @SerializedName("last_name")
    @Expose
    private String last_name;

    @SerializedName("email_id")
    @Expose
    private String email_id;

    @SerializedName("mobile")
    @Expose
    private String mobile;

    @SerializedName("member_id")
    @Expose
    private int member_id;


    @SerializedName("id")
    @Expose
    private int id;

    @SerializedName("member_token")
    @Expose
    private String member_token;


    @SerializedName("role")
    @Expose
    private String role;



    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String firstName) {
        this.first_name = firstName;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String lastName) {
        this.last_name = lastName;
    }

    public String getEmailId() {
        return email_id;
    }

    public void setEmailId(String emailId) {
        this.email_id = emailId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getMemberId() {
        return member_id;
    }

    public void setMemberId(int memberId) {
        this.member_id = memberId;
    }

    public String getMemberToken() {
        return member_token;
    }

    public void setMemberToken(String memberToken) {
        this.member_token = memberToken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }


}
