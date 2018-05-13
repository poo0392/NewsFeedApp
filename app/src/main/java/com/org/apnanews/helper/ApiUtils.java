package com.org.apnanews.helper;

import com.org.apnanews.interfaces.WebService;

/**
 * Created by Pooja.Patil on 13/03/2018.
 */

public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "http://thanehousingfederation.com/newsapp/api/member/";

    public static WebService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(WebService.class);
    }
}
