package MyHandler;

import com.alibaba.fastjson.JSON;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpResponse;
import pojo.Message;
import pojo.Status;

import static io.netty.handler.codec.http.HttpHeaderNames.ACCESS_CONTROL_ALLOW_ORIGIN;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static org.apache.http.HttpHeaders.CONTENT_LENGTH;

public class OutHandler extends ChannelOutboundHandlerAdapter {

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        Status status= (Status) msg;
        String res=JSON.toJSONString(status);
        FullHttpResponse response = null;
        response = new DefaultFullHttpResponse(HTTP_1_1,
                OK, Unpooled.wrappedBuffer(res.getBytes("UTF-8")));
        response.headers().set(CONTENT_TYPE, "application/json");
        response.headers().setInt(CONTENT_LENGTH,
                response.content().readableBytes());
        response.headers().set(ACCESS_CONTROL_ALLOW_ORIGIN,"*");
        ctx.write(response);
        ctx.flush();

    }


}
