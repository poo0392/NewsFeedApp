package job.com.news.models;

import android.graphics.drawable.Drawable;

/**
 * Created by POOJA on 1/25/2018.
 */

public class NewsFeedDetails {
    private String news_category;
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

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }

    public String getNews_category() {
        return news_category;
    }

    public void setNews_category(String news_category) {
        this.news_category = news_category;
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

        /*public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }*/

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
}