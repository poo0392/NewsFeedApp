package job.com.news.register;

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
    private Integer member_id;

    @SerializedName("member_token")
    @Expose
    private String member_token;

    public String getFirstName() {
        return first_name;
    }

    public void setFirstName(String firstName) {
        this.first_name = first_name;
    }

    public String getLastName() {
        return last_name;
    }

    public void setLastName(String lastName) {
        this.last_name = last_name;
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

    public Integer getMemberId() {
        return member_id;
    }

    public void setMemberId(Integer memberId) {
        this.member_id = member_id;
    }

    public String getMemberToken() {
        return member_token;
    }

    public void setMemberToken(String memberToken) {
        this.member_token = member_token;
    }

}
