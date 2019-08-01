package com.chenshinan.exercises.rocketchat.rest.config;

import com.chenshinan.exercises.rocketchat.rest.RestMain;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

public class AddCookiesInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        String authToken = RestMain.headerMap.get("X-Auth-Token");
        String userId = RestMain.headerMap.get("X-User-Id");
        if (authToken != null) {
            builder.addHeader("X-Auth-Token", authToken);
        }
        if (userId != null) {
            builder.addHeader("X-User-Id", userId);
        }
        return chain.proceed(builder.build());
    }
}