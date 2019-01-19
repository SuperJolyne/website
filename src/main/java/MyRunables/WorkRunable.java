package MyRunables;

import Myservlet.Myservlet;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderUtil;
import pojo.D;
import pojo.Message;
import pojo.Status;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;

import static io.netty.handler.codec.http.HttpHeaderNames.*;
import static io.netty.handler.codec.http.HttpHeaderNames.KEEP_ALIVE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class WorkRunable implements Runnable{

    private Message message=null;

    private ChannelHandlerContext ctx=null;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public ChannelHandlerContext getCtx() {
        return ctx;
    }

    public void setCtx(ChannelHandlerContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void run() {
        D d = JSON.parseObject(message.getData(), D.class);

        try {
        Status status = Myservlet.doServlet(d,message.getUri());

        String res = JSONObject.toJSONString(status);
            System.out.println(res);
        FullHttpResponse response = null;

            response = new DefaultFullHttpResponse(HTTP_1_1,
                    OK, Unpooled.wrappedBuffer(res.getBytes("UTF-8")));

        response.headers().set(CONTENT_TYPE, "application/json");
        response.headers().setInt(CONTENT_LENGTH,
                response.content().readableBytes());
        response.headers().set(ACCESS_CONTROL_ALLOW_ORIGIN,"*");
        if (HttpHeaderUtil.isKeepAlive(this.message.getFhr())) {
            response.headers().set(CONNECTION, KEEP_ALIVE);
        }

        this.ctx.write(response);
        this.ctx.flush();

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
