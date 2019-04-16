package server;

import consistencyHash.MyHttp.*;
import consistencyHash.Myqueue.*;

import java.io.IOException;

public class Start {
    public static void main(String[] args) throws InterruptedException, IOException {
        //启动netty服务
        Thread inbound = new Thread(new Runnable() {
            @Override
            public void run() {
                Server serverIn = new Server();
                try {
                    serverIn.startinbound(7777);
                } catch (Exception e) {
//                    log.error("Inbound Server crash!!!", e);
                    System.exit(1);
                }
            }
        });
        inbound.start();

        //启动内部的redis添加节点通信服务,等待添加节点
        Thread http_thread = new Thread(new Runnable() {
            @Override
            public void run() {
                MyHttpServer server = new MyHttpServer();
                try {
                    server.myHttpserverServer(7774);
                    System.out.println("server启动");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        http_thread.start();

        //redis添加节点的消费者
        Redis_Consume r = new Redis_Consume(RedisQueue.queue);
        r.start();
        System.out.println("启动完成");
    }
}
