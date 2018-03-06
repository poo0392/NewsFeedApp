package job.com.news.interfaces;

import java.util.List;

import job.com.news.article.City;
import job.com.news.article.State;
import job.com.news.forgotpassword.ForgotPasswordResp;
import job.com.news.models.NewsFeedModelResponse;
import job.com.news.register.LoginRegisterResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

/**
 * Created by deepak on 5/16/2017.
 */

public interface WebService {
    //changes added on 09/02
    //to get state list
    @GET("states")
    Call<State> getStateList();

    /*//to get city list
    @Multipart
    @POST("indian-city-by-state")
    Call<City> getCityList(@Part("project-app-key") RequestBody project_app_key,
                           @Part("stateid") RequestBody stateid);*/

    //to get city list
    @GET("cities_on_stateid/{id}")
    Call<City> getCityList(@Path("id") int state_id);
    // Call<City> getCityList();

    //http://thanehousingfederation.com/abhi/api/member/register
    //register
    @Multipart
    @POST("register")
    Call<LoginRegisterResponse> registerRequest(@Part("first_name") RequestBody first_name,
                                                @Part("last_name") RequestBody last_name,
                                                @Part("mobile") RequestBody mobile,
                                                @Part("password") RequestBody Password,
                                                @Part("Notification_id") RequestBody Notification_id,
                                                @Part("platform") RequestBody platform,
                                                @Part("email") RequestBody email);

    //http://thanehousingfederation.com/newsapp/api/member/login
    //login
    @Multipart
    @POST("login")
    Call<LoginRegisterResponse> loginRequest(@Part("email") RequestBody email,
                                             @Part("password") RequestBody password,
                                             @Part("Notification_id") RequestBody Notification_id,
                                             @Part("platform") RequestBody platform);


    //http://thanehousingfederation.com/newsapp/api/member/forgotpassword
    //login
    @Multipart
    @POST("forgotpassword")
    Call<ForgotPasswordResp> forgotPasswordRequest(@Part("email_id") RequestBody email_id);

    //http://thanehousingfederation.com/newsapp/api/member/forgotpassword
    //login
    @Multipart
    @POST("changepassword")
    Call<ForgotPasswordResp> changePasswordRequest(@Part("member_id") RequestBody member_id,
                                                   @Part("member_token") RequestBody member_token,
                                                   @Part("new_password") RequestBody new_password);

    //http://thanehousingfederation.com/newsapp/api/member/news_list
    //@GET("member_token/{member_token}/member_id/{member_id}")

    //changes added on 09/02
    @Multipart
    @POST("news_list")
    Call<NewsFeedModelResponse> getNewsListRequest(@Part("member_token") RequestBody member_token,
                                                   @Part("member_id") RequestBody member_id);


    //add_news
    //http://thanehousingfederation.com/newsapp/api/member/add_news

    @Multipart
    @POST("add_news")
//changes 06_03
    Call<NewsFeedModelResponse> post_news(@Part("member_token") RequestBody member_token,
                                          @Part("member_id") RequestBody member_id,
                                          @Part("category_id") RequestBody category_id,
                                          @Part("country_id") RequestBody country_id,
                                          @Part("state_id") RequestBody state_id,
                                          @Part("city_id") RequestBody city_id,
                                          @Part("news_title") RequestBody news_title,
                                          @Part("news_desc") RequestBody news_desc,
                                         // @Part("news_pic") RequestBody news_pic,
                                          //@Part("news_pic") String[] news_pic);
                                          @Part("news_pic") List<String> news_pic);
                                         // @Body String[] news_pic);





}
