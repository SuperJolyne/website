package consistencyHash.MyHttp;

import consistencyHash.Myqueue.*;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.spi.HttpServerProvider;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;

public class MyHttpServer {

    public void myHttpserverServer(int port) throws IOException {

        HttpServerProvider provider = HttpServerProvider.provider();
        HttpServer server = provider.createHttpServer(new InetSocketAddress(port),100);
        server.createContext("/redis", new MyRedisHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("start");
    }

    static class MyRedisHandler implements HttpHandler{

        public void handle(HttpExchange httpExchange) throws IOException {
            InputStream in = httpExchange.getRequestBody();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String temp = reader.readLine();

            Redis_Product p = new Redis_Product(RedisQueue.queue);
            p.run(temp);

            httpExchange.sendResponseHeaders(200,0);
            httpExchange.close();
        }
    }
}
