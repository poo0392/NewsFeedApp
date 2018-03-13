package job.com.news.helper;

import job.com.news.interfaces.WebService;

/**
 * Created by Zafar.Hussain on 13/03/2018.
 */

public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "http://thanehousingfederation.com/newsapp/api/member/";

    public static WebService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(WebService.class);
    }
}
