package pojo;

import io.netty.handler.codec.http.FullHttpRequest;

public class Message {

    FullHttpRequest fhr=null;

    private String uri;

    private String data;

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public FullHttpRequest getFhr() {
        return fhr;
    }

    public void setFhr(FullHttpRequest fhr) {
        this.fhr = fhr;
    }
}
