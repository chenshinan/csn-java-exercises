package com.chenshinan.exercises.rocketchat;

import com.github.daniel_sc.rocketchat.modern_client.RocketChatClient;
import com.github.daniel_sc.rocketchat.modern_client.response.ChatMessage;
import com.github.daniel_sc.rocketchat.modern_client.response.Room;
import com.github.daniel_sc.rocketchat.modern_client.response.Subscription;
import io.reactivex.Observable;

import java.util.List;

/**
 * @author shinan.chen
 * @since 2019/6/23
 */
public class ChatMain {
    public static void main(String[] args) {
        String URL = "ws://localhost:3000/websocket";
        try (MyClient client = new MyClient(URL, "admin", "csn000000")) {
            //获取房间
            List<Room> rooms = client.getRooms().join();
            if(!rooms.isEmpty()){
                //发送消息
                ChatMessage msg = client.sendMessageExtendedParams("Your message", rooms.get(0).id, null, null, ":e-mail:", null).join();
                System.out.println(msg);
                //获取所有信息
                String messages = client.getMessages(rooms.get(0).id,null,50,null).join();
                System.out.println(messages);
            }
            //获取订阅
            List<Subscription> subscriptions = client.getSubscriptions().join();
            System.out.println(subscriptions);


        }
    }
}
