package MyHandler;

import Myservlet.Myservlet;
import com.alibaba.fastjson.JSON;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaders;
import pojo.D;
import pojo.Message;
import pojo.Status;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;

public class InHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException {
        Message message = new Message();
        FullHttpRequest fhr = (FullHttpRequest) msg;
        message.setUri(fhr.uri());
        message.setFhr(fhr);
        ByteBuf buf = fhr.content();
        HttpHeaders head=fhr.headers();
        byte[] result1 = new byte[buf.readableBytes()];
        buf.readBytes(result1);
        String data=new String(result1,"utf8");
        System.out.println("----------------------------读取的数据："+data);
        message.setData(data);
        D d1 = JSON.parseObject(message.getData(), D.class);
        try {
            Status status = Myservlet.doServlet(d1,message.getUri());
            ctx.write(status);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

//        WorkRunable work = new WorkRunable();
//        work.setMessage(message);
//        work.setCtx(ctx);
//        MyThreadPool.doWork(work);

    }
}
