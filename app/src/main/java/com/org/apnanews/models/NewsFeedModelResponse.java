package com.org.apnanews.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by POOJA on 1/25/2018.
 */

public class NewsFeedModelResponse {
    //changes added on 09/02

    @SerializedName("status")
    @Expose
    private Integer status;


    @SerializedName("code")
    @Expose
    private String code;

    @SerializedName("description")
    @Expose
    private String description;


    @SerializedName("news")
    @Expose
    // private NewsFeedList newsFeedList;
    private List<NewsFeedList> newsFeedList;
 /*   private List<NewsFeedListParcable> newsFeedListParc;

    public List<NewsFeedListParcable> getNewsFeedListParc() {
        return newsFeedListParc;
    }

    public void setNewsFeedListParc(List<NewsFeedListParcable> newsFeedListParc) {
        this.newsFeedListParc = newsFeedListParc;
    }
*/

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


    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
   /* public NewsFeedList getNewsFeedList() {
        return newsFeedList;
    }

    public void setNewsFeedList(NewsFeedList newsFeedList) {
        this.newsFeedList = newsFeedList;
    }*/





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
