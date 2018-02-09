package job.com.news.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Zafar.Hussain on 08/02/2018.
 */

public class UserModel {
    //changes added on 09/02

    @SerializedName("id")
    @Expose
    private int id;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getEmail_id() {
        return email_id;
    }

    public void setEmail_id(String email_id) {
        this.email_id = email_id;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }
}
