package Osscli;
import com.aliyun.oss.OSSClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;

public class App {

    public static OSSClient getOSS(){
        // Endpoint以杭州为例，其它Region请按实际情况填写。
        String endpoint = "http://oss-cn-shenzhen.aliyuncs.com/";
        // 阿里云主账号AccessKey拥有所有API的访问权限，风险很高。强烈建议您创建并使用RAM账号进行API访问或日常运维，请登录 https://ram.console.aliyun.com 创建RAM账号。
        String accessKeyId = "LTAI39GscXA3TzaQ";
        String accessKeySecret = "DmPvgIQ4yRLmqWand0HEbQG6DWld1I";
        String bucketName = "superljc";
        OSSClient ossClient = new OSSClient(endpoint,accessKeyId, accessKeySecret);

        return ossClient;
    }


    public static void main(String[] args) throws IOException {
        InputStream inputStream = new URL("https://s3-eu-west-1.amazonaws.com/img.frmoda.com/scarpe/carshoe/kut7/alias/KUT7311O7JF0002nero-01.jpg").openStream();
        OSSClient ossClient = getOSS();
        ossClient.putObject("my0039t","my0039/page/com/xxx/xxx.jpg",inputStream);
        ossClient.shutdown();
        Date expiration = new Date(new Date().getTime() + 3600l * 1000 * 24 * 365 * 10);
        String url = ossClient.generatePresignedUrl("my0039t", "my0039/page/com/xxx/xxx.jpg", expiration).toString();
        System.out.println(url);
    }
}
