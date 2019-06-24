package com.chenshinan.exercises.rocketchat;

import com.github.daniel_sc.rocketchat.modern_client.RocketChatClient;
import com.github.daniel_sc.rocketchat.modern_client.request.MethodRequest;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * @author shinan.chen
 * @since 2019/6/23
 */
public class MyClient extends RocketChatClient {
    public MyClient(String url, String user, String password) {
        super(url, user, password);
    }

    public MyClient(String url, String user, String password, Executor executor) {
        super(url, user, password, executor);
    }

    public CompletableFuture<String> getMessages(String rid, String date1, Integer count, String date2) {
        MethodRequest request = new MethodRequest("loadHistory", rid, date1, count, date2);
        return send(request, failOnError(r -> r.result.toString()));
    }
}
