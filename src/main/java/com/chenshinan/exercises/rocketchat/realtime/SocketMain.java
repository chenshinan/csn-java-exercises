package com.chenshinan.exercises.rocketchat.realtime;

import org.java_websocket.WebSocket;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.UUID;

/**
 * @author shinan.chen
 * @since 2019/8/1
 */
public class SocketMain {
    public static void main(String[] args) {
        MyWebSocketClient myClient = null;
        try {
            myClient = new MyWebSocketClient(new URI("ws://localhost:3000/websocket"));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        myClient.connect();
        while (!myClient.getReadyState().equals(WebSocket.READYSTATE.OPEN)) {
            System.out.println("还没有打开");
        }
        System.out.println("打开了");
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        myClient.send("{\n" +
                "\t\"msg\": \"connect\",\n" +
                "\t\"version\": \"1\",\n" +
                "\t\"support\": [\"1\", \"pre2\", \"pre1\"]\n" +
                "}");
        //登陆
        myClient.send("{\n" +
                "    \"msg\": \"method\",\n" +
                "    \"method\": \"login\",\n" +
                "    \"id\":\""+ UUID.randomUUID().toString()+"\",\n" +
                "    \"params\":[\n" +
                "        {\n" +
                "            \"user\": {         \"username\":\"cstt\" },\n" +
                "            \"password\": {\n" +
                "                \"digest\":\"6237c385db8f889e133c86844472f606051e1b3eebe6f08e1ec4b5a9ff73b3ee\",\n" +
                "                \"algorithm\":\"sha-256\"\n" +
                "            }\n" +
                "        }\n" +
                "    ]\n" +
                "}");
        //获取历史记录
        myClient.send("{\n" +
                "    \"msg\": \"method\",\n" +
                "    \"method\": \"loadHistory\",\n" +
                "    \"id\": \""+ UUID.randomUUID().toString()+"\",\n" +
                "    \"params\": [ \"GENERAL\", null, 50, { \"$date\": 1480377601 } ]\n" +
                "}");
        //获取房间消息流
        myClient.send("{\n" +
                "    \"msg\": \"sub\",\n" +
                "    \"id\": \""+ UUID.randomUUID().toString()+"\",\n" +
                "    \"name\": \"stream-room-messages\",\n" +
                "    \"params\":[\n" +
                "        \"GENERAL\",\n" +
                "        false\n" +
                "    ]\n" +
                "}");
        //发送消息
        myClient.send("{\n" +
                "    \"msg\": \"method\",\n" +
                "    \"method\": \"sendMessage\",\n" +
                "    \"id\": \""+ UUID.randomUUID().toString()+"\",\n" +
                "    \"params\": [\n" +
                "        {\n" +
                "            \"_id\": \"100\",\n" +
                "            \"rid\": \"GENERAL\",\n" +
                "            \"msg\": \"Hello World!\"\n" +
                "        }\n" +
                "    ]\n" +
                "}");
        //回复消息
        myClient.send("{\n" +
                "    \"msg\": \"method\",\n" +
                "    \"method\": \"sendMessage\",\n" +
                "    \"id\": \""+ UUID.randomUUID().toString()+"\",\n" +
                "    \"params\": [\n" +
                "        {\n" +
                "            \"_id\": \""+ UUID.randomUUID().toString()+"\",\n" +
                "            \"rid\": \"GENERAL\",\n" +
                "            \"tmid\": \"100\",\n" +
                "            \"msg\": \"reply message\"\n" +
                "        }\n" +
                "    ]\n" +
                "}");
    }
}
