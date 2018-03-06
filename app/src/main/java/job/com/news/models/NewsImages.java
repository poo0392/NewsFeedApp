package job.com.news.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Pooja.Patil on 06/03/2018.
 */

public class NewsImages {
    //changes 06_03
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("news_id")
    @Expose
    private String news_id;
    @SerializedName("news_pic")
    @Expose
    private String news_pic;
    @SerializedName("created_at")
    @Expose
    private String created_at;
    @SerializedName("updated_at")
    @Expose
    private String updated_at;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNews_id() {
        return news_id;
    }

    public void setNews_id(String news_id) {
        this.news_id = news_id;
    }

    public String getNews_pic() {
        return news_pic;
    }

    public void setNews_pic(String news_pic) {
        this.news_pic = news_pic;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }
}
