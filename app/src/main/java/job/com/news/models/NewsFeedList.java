package job.com.news.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import job.com.news.register.RegisterMember;

/**
 * Created by Pooja.Patil on 12/02/2018.
 */

public class NewsFeedList {
    //changes 06_03

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("news_uuid")
    @Expose
    private String news_uuid;
    @SerializedName("category")
    @Expose
    private String category; //category
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("state")
    @Expose
    private String state;
    @SerializedName("city")
    @Expose
    private String city;
    @SerializedName("news_title")
    @Expose
    private String news_title;
    @SerializedName("news_description")
    @Expose
    private String news_description;
    @SerializedName("news_pic")
    @Expose
    private String news_pic;
    @SerializedName("like_count")
    @Expose
    private String like_count;
    @SerializedName("member_id")
    @Expose
    private String member_id;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    //private RegisterMember member;
        /*@SerializedName("user")
        @Expose
        private UserModel user;*/


    @SerializedName("news_images")
    @Expose
    private List<NewsImages> news_images;
    @SerializedName("member")
    @Expose
    private RegisterMember member;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNews_uuid() {
        return news_uuid;
    }

    public void setNews_uuid(String news_uuid) {
        this.news_uuid = news_uuid;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNews_title() {
        return news_title;
    }

    public void setNews_title(String news_title) {
        this.news_title = news_title;
    }

    public String getNews_description() {
        return news_description;
    }

    public void setNews_description(String news_description) {
        this.news_description = news_description;
    }

    public List<NewsImages> getNews_images() {
        return news_images;
    }

    public void setNews_images(List<NewsImages> news_images) {
        this.news_images = news_images;
    }

    public String getNews_pic() {
        return news_pic;
    }

    public void setNews_pic(String news_pic) {
        this.news_pic = news_pic;
    }

    public String getLike_count() {
        return like_count;
    }

    public void setLike_count(String like_count) {
        this.like_count = like_count;
    }

    public String getMember_id() {
        return member_id;
    }

    public void setMember_id(String member_id) {
        this.member_id = member_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    /* public UserModel getUser() {
         return user;
     }

     public void setUser(UserModel user) {
         this.user = user;
     }*/
    public RegisterMember getMember() {
        return member;
    }

    public void setMember(RegisterMember member) {
        this.member = member;
    }

    @Override
    public String toString() {
        return "NewsFeedList{" +
                "id=" + id +
                ", news_uuid='" + news_uuid + '\'' +
                ", category='" + category + '\'' +
                ", country='" + country + '\'' +
                ", state='" + state + '\'' +
                ", city='" + city + '\'' +
                ", news_title='" + news_title + '\'' +
                ", news_description='" + news_description + '\'' +
                ", news_pic='" + news_pic + '\'' +
                ", like_count='" + like_count + '\'' +
                ", member_id='" + member_id + '\'' +
                ", created_at='" + created_at + '\'' +
                '}';
    }

        /* public RegisterMember getMember() {
            return member;
        }

        public void setMember(RegisterMember member) {
            this.member = member;
        }*/
}
