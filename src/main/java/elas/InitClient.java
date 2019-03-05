package elas;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;

/**
 * @author jijngbao
 * @date 19-1-13
 */
public class InitClient {
    public static RestHighLevelClient getClients() {

        RestHighLevelClient client = new RestHighLevelClient(
                RestClient.builder(new HttpHost("super0", 9200, "http")));

        return client;
    }

    public static RestHighLevelClient getClient(){
        RestHighLevelClient client=new RestHighLevelClient(RestClient.builder
                (new HttpHost("super0",9200,"http")));
        return client;
    }
}
