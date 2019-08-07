package com.chenshinan.exercises.rocketchat.realtime;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URI;

/**
 * @author shinan.chen
 * @since 2019/8/1
 */
public class MyWebSocketClient extends WebSocketClient {

    private Logger logger = LoggerFactory.getLogger(MyWebSocketClient.class);

    public MyWebSocketClient(URI serverUri) {
        super(serverUri);
    }

    @Override
    public void onOpen(ServerHandshake arg0) {
        logger.info("------ MyWebSocket onOpen ------");
    }

    @Override
    public void onClose(int arg0, String arg1, boolean arg2) {
        // TODO Auto-generated method stub
        logger.info("------ MyWebSocket onClose ------");
    }

    @Override
    public void onError(Exception arg0) {
        // TODO Auto-generated method stub
        logger.info("------ MyWebSocket onError ------");
    }

    @Override
    public void onMessage(String arg0) {
        // TODO Auto-generated method stub
        logger.info("-------- 接收到服务端数据： " + arg0 + "--------");
        if(arg0.equals("{\"msg\":\"ping\"}")){
            logger.info("-------- 客户端发送数据： {\"msg\":\"pong\"}--------");
            this.send("{\"msg\":\"pong\"}");
        }
    }
}