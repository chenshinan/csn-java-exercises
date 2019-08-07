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
                .addInterceptor(new AddCookiesInterceptor())
                .build();
        //创建 Retrofit 实例
        Retrofit retrofit = new Retrofit.Builder()
                //设置网络请求url地址
                .baseUrl("http://localhost:3000")
                //设置数据解析器
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
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
        //获取频道列表
        Map<String, Object> channels = request("获取频道列表", requestInterface.getChannelsList(null));
        String rid = (String) ((Map) ((List) channels.get("channels")).get(0)).get("_id");
        String rname = (String) ((Map) ((List) channels.get("channels")).get(0)).get("name");
        //发送聊天信息
        Map<String, Object> sendMessage = request("发送聊天信息", requestInterface.sendMessage(new SendMessage(SendMessageParam.forSendMessage(null, "from java", rid))));
        String mid = (String) ((Map) sendMessage.get("message")).get("_id");
        //举报聊天信息
        Map<String, Object> reportMessage = request("举报聊天信息", requestInterface.reportMessage(new ReportMessage(mid, "report java")));
        //根据房间id获取所有聊天信息
        Map<String, Object> allMessage = request("根据房间id获取所有聊天信息", requestInterface.getAllMessageByRid(rid));
        //创建讨论
        Map<String, Object> createDiscussion = request("创建讨论", requestInterface.createDiscussion(new Discussion(rid, "新讨论")));
        //创建用户
        Map<String, Object> createUser = request("创建用户", requestInterface.createUser(new UserCreate("cstt", "574466609@qq.com", "csn000000", "cstt")));
        //给房间添加自定义字段
        ChannelCustField channelCustField = new ChannelCustField();
        channelCustField.setRoomId(rid);
        channelCustField.setRoomName(rname);
        Map<String, String> cusFieldMap = new HashMap<>(2);
        cusFieldMap.put("organizationId", "1");
        channelCustField.setCustomFields(cusFieldMap);
        Map<String, Object> channelSetCustomFields = request("给房间添加自定义字段", requestInterface.channelSetCustomFields(channelCustField));
        //获取频道列表，根据自定义搜索
        Map<String, Object> channelsBySearch = request("获取频道列表，根据自定义搜索", requestInterface.getChannelsList("{ \"customFields.organizationId\": { \"$eq\": \"1\" } }"));
    }

    private static Map<String, Object> request(String log, Call<String> call) {
        System.out.println(log);
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
