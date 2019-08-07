package com.chenshinan.exercises.rocketchat.rest.inter;

import com.chenshinan.exercises.rocketchat.rest.dto.*;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * @author shinan.chen
 * @since 2019/3/3
 */
public interface RequestInterface {
    @POST("api/v1/login")
    Call<String> userLogin(@Body UserLogin userLogin);

    @GET("api/v1/channels.list")
    Call<String> getChannelsList(@Query("query")String query);

    @POST("api/v1/chat.sendMessage")
    Call<String> sendMessage(@Body SendMessage message);

    @POST("api/v1/chat.reportMessage")
    Call<String> reportMessage(@Body ReportMessage message);

    @GET("api/v1/channels.messages")
    Call<String> getAllMessageByRid(@Query("roomId") String roomId);

    @POST("api/v1/rooms.createDiscussion")
    Call<String> createDiscussion(@Body Discussion discussion);

    @POST("api/v1/users.create")
    Call<String> createUser(@Body UserCreate create);

    @POST("api/v1/channels.setCustomFields")
    Call<String> channelSetCustomFields(@Body ChannelCustField channelCustField);
}
