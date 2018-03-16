package job.com.news.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import job.com.news.register.RegisterMember;

/**
 * Created by Pooja.Patil on 12/02/2018.
 */

public class NewsFeedList implements Parcelable{

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("news_uuid")
    @Expose
    private String news_uuid;
    @SerializedName("category")
    @Expose
    private String category; //category

    @SerializedName("cat_id")
    @Expose
    private String category_id; //category
    @SerializedName("sub_category")
    @Expose
    private String sub_category;
    @SerializedName("sub_cat_id")
    @Expose
    private String sub_category_id;
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
    @SerializedName("news_images")
    @Expose
    private List<NewsImages> news_images;

    @SerializedName("member")
    @Expose
    private RegisterMember member;
   // private  List<RegisterMember> membersList;


    public NewsFeedList(int id, String news_uuid, String category,String category_id, String sub_category, String sub_category_id,String country, String state, String city, String news_title, String news_description, String like_count, String member_id, String created_at,List<NewsImages> news_images,RegisterMember member) {
        this.id = id;
        this.news_uuid = news_uuid;
        this.category = category;
        this.category_id = category_id;
        this.sub_category = sub_category;
        this.sub_category_id = sub_category_id;
        this.country = country;
        this.state = state;
        this.city = city;
        this.news_title = news_title;
        this.news_description = news_description;
        this.like_count = like_count;
        this.member_id = member_id;
        this.created_at = created_at;
        this.news_images = news_images;
        this.member = member;
    }
    public NewsFeedList(int id, String news_uuid, String category ,String category_id, String sub_category, String sub_category_id, String country, String state, String city, String news_title, String news_description, String like_count, String member_id, String created_at) {
        this.id = id;
        this.news_uuid = news_uuid;
        this.category = category;
        this.category_id = category_id;
        this.sub_category = sub_category;
        this.sub_category_id = sub_category_id;
        this.country = country;
        this.state = state;
        this.city = city;
        this.news_title = news_title;
        this.news_description = news_description;
        this.like_count = like_count;
        this.member_id = member_id;
        this.created_at = created_at;
    }
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

    public String getCategory_id() {
        return category_id;
    }

    public void setCategory_id(String category_id) {
        this.category_id = category_id;
    }

    public String getSub_category_id() {
        return sub_category_id;
    }

    public void setSub_category_id(String sub_category_id) {
        this.sub_category_id = sub_category_id;
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

   public String getSub_category() {
       return sub_category;
   }

    public void setSub_category(String sub_category) {
        this.sub_category = sub_category;
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

    public RegisterMember getMember() {
        return member;
    }

    public void setMember(RegisterMember member) {
        this.member = member;
    }


    protected NewsFeedList(Parcel in) {
        id = in.readInt();
        news_uuid = in.readString();
        category = in.readString();
        category_id = in.readString();
        sub_category = in.readString();
        sub_category_id = in.readString();
        country = in.readString();
        state = in.readString();
        city = in.readString();
        news_title = in.readString();
        news_description = in.readString();
        like_count = in.readString();
        member_id = in.readString();
        created_at = in.readString();
    }

    public static final Creator<NewsFeedList> CREATOR = new Creator<NewsFeedList>() {
        @Override
        public NewsFeedList createFromParcel(Parcel in) {
            return new NewsFeedList(in);
        }

        @Override
        public NewsFeedList[] newArray(int size) {
            return new NewsFeedList[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(news_uuid);
        dest.writeString(category);
        dest.writeString(category_id);
        dest.writeString(sub_category);
        dest.writeString(sub_category_id);
        dest.writeString(country);
        dest.writeString(state);
        dest.writeString(city);
        dest.writeString(news_title);
        dest.writeString(news_description);
        dest.writeString(like_count);
        dest.writeString(member_id);
        dest.writeString(created_at);
    }

   /* public List<RegisterMember> getMembersList() {
        return membersList;
    }

    public void setMembersList(List<RegisterMember> membersList) {
        this.membersList = membersList;
    }*/


}
