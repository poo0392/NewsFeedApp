package job.com.news.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import job.com.news.register.RegisterMember;

/**
 * Created by POOJA on 1/25/2018.
 */

public class NewsFeedModelResponse {
    //changes added on 09/02

    @SerializedName("status")
    @Expose
    private Integer status;
    // @SerializedName("description")
    // @Expose
    private String description;


    @SerializedName("news")
    @Expose
    // private NewsFeedList newsFeedList;
    private List<NewsFeedList> newsFeedList;

    public List<NewsFeedList> getNewsFeedList() {
        return newsFeedList;
    }

    public void setNewsFeedList(List<NewsFeedList> newsFeedList) {
        this.newsFeedList = newsFeedList;
    }

    // private List<NewsFeedList> newsFeedList;

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

   /* public NewsFeedList getNewsFeedList() {
        return newsFeedList;
    }

    public void setNewsFeedList(NewsFeedList newsFeedList) {
        this.newsFeedList = newsFeedList;
    }*/


    public class NewsFeedList {
        //changes added on 09/02

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


    /*public List<NewsFeedDetails> getNewsFeedDetailsList() {
        return newsFeedDetailsList;
    }

    public void setNewsFeedDetailsList(List<NewsFeedDetails> newsFeedDetailsList) {
        this.newsFeedDetailsList = newsFeedDetailsList;
    }*/
/*
    public class NewsFeedDetails {
        private String category;
        private String title;
        private String description;
       // private String image;// in string format
        private Drawable image;// for static
        private String date;
        private String time;
        private String city;
        private String state;
        private String news_post_person;

        public NewsFeedDetails(String title, String time, String date, Drawable image,String city,String state,String news_post_person ) {
            this.title = title;
            this.time = time;
            this.date = date;
            this.image = image;
            this.city = city;
            this.state = state;
            this.news_post_person = news_post_person;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        *//*public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }*//*

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getNews_post_person() {
            return news_post_person;
        }

        public void setNews_post_person(String news_post_person) {
            this.news_post_person = news_post_person;
        }
    }*/
}
