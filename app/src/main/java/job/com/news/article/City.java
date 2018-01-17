package job.com.news.article;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by deepak on 5/16/2017.
 */
import java.util.List;

public class City {

    private Integer status;
    private String description;
    private List<City_Name> cities = null;

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

    public List<City_Name> getCities() {
        return cities;
    }

    public void setCities(List<City_Name> cities) {
        this.cities = cities;
    }

}