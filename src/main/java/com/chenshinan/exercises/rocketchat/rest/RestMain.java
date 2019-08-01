package com.chenshinan.exercises.rocketchat.rest;

import com.chenshinan.exercises.rocketchat.rest.config.AddCookiesInterceptor;
import com.chenshinan.exercises.rocketchat.rest.dto.*;
import com.chenshinan.exercises.rocketchat.rest.inter.RequestInterface;
import com.chenshinan.exercises.rocketchat.rest.utils.Json2Map;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author shinan.chen
 * @since 2019/7/31
 */
public class RestMain {
    public static final Logger LOGGER = LoggerFactory.getLogger(RestMain.class);
    public static Map<String, String> headerMap = new HashMap<>();

    public static void main(String[] args) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new AddCookiesInterceptor()) //这部分
                .build();

        //创建 Retrofit 实例
        Retrofit retrofit = new Retrofit.Builder()
                //设置网络请求url地址
                .baseUrl("http://localhost:3000")
                //设置数据解析器
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                //设置网络请求适配器
//                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        //创建网络请求接口实例
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        //获取封装的请求
        UserLogin login = new UserLogin();
        login.setUser("admin");
        login.setPassword("csn000000");
        Call<String> call = requestInterface.userLogin(login);
        //发送网络请求(同步)
        try {
            Response<String> response = call.execute();
            System.out.println(response.body());
            headerMap.put("X-Auth-Token", ((Map<String, String>) (new Gson().fromJson(response.body(), Map.class).get("data"))).get("authToken"));
            headerMap.put("X-User-Id", ((Map<String, String>) (new Gson().fromJson(response.body(), Map.class).get("data"))).get("userId"));
            System.out.println(headerMap);
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("获取频道列表");
        Map<String, Object> channels = request(requestInterface.getChannelsList());
        String rid = (String) ((Map) ((List) channels.get("channels")).get(0)).get("_id");

        System.out.println("发送聊天信息");
        Map<String, Object> sendMessage = request(requestInterface.sendMessage(new SendMessage(SendMessageParam.forSendMessage(null, "from java", rid))));
        String mid = (String) ((Map) sendMessage.get("message")).get("_id");
//        System.out.println("回复聊天信息【没有效果】");
//        Map<String, Object> replyMessage = request(requestInterface.sendMessage(new SendMessage(SendMessageParam.forSendMessage(mid, "reply java", rid))));
        System.out.println("举报聊天信息");
        Map<String, Object> reportMessage = request(requestInterface.reportMessage(new ReportMessage(mid, "report java")));

        System.out.println("根据房间id获取所有聊天信息");
        Map<String, Object> allMessage = request(requestInterface.getAllMessageByRid(rid));

        System.out.println("创建讨论");
        Map<String, Object> createDiscussion = request(requestInterface.createDiscussion(new Discussion(rid, "新讨论")));

        System.out.println("回复聊天信息");
        Map<String, Object> replyMessage = request(requestInterface.replyMessage(mid, new SendMessage(SendMessageParam.forSendMessage(null, "reply java", rid))));
        String mmid = (String) ((Map) sendMessage.get("message")).get("_id");
        System.out.println("finish");
    }

    private static Map<String, Object> request(Call<String> call) {
        try {
            Response<String> response = call.execute();
            System.out.println(response.body());
            return Json2Map.json2Map(response.body());
        } catch (Exception e) {
            LOGGER.error(e.getMessage());
        }
        return null;
    }
}
