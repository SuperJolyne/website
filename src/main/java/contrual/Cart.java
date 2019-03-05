package contrual;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import elas.SearchDocument;
import pojo.D;
import pojo.JudgeUid;
import pojo.Sign;
import pojo.Status;
import util.RedisOperating;

import java.io.IOException;
import java.util.*;

public class Cart {

    //    private static Logger logger = Logger.getLogger(Register.class);

    public static Status view(D d){
        Status status = new Status();
        status.setRs("t");
        status.newDd();
        String id = d.getUid();
        boolean b = JudgeUid.jud(id);
        if (b == false){
            status.setRs("f");
            return status;
        }
        String uid = id.split("\\|")[0];
        String cart = uid+"_cart";
        System.out.println(cart);
        int pag = Integer.parseInt(d.getPag());
        int start = (pag-1)*10;
        int end = start+10;
        //实现分页
        Map<String, String> map = RedisOperating.hgetAll(cart);
        Set<String> set = map.keySet();
        List<String> list = new ArrayList<>();
        for(String s : set){
            list.add(s);
        }
        if (list.size()<end){
            list = list.subList(start, list.size());
        }else {
            list = list.subList(start, end);
        }

        //
        for (String s : list){
            String qua = (String)RedisOperating.hget(cart, s);
            String gid = s.split("\\|")[0];
            String siz = s.split("\\|")[1];
            try {
                List<String> jsonList = SearchDocument.
                        groupSearch("my0039","goods", "{\"gid\":\""+gid+"\"}",0,0,30000,40);
                for (String s1 : jsonList){
                    JSONObject j = JSONObject.parseObject(s1);
                    String nam = j.getString("title");
                    String col = j.getString("color");
                    String pri = j.getString("price");
                    String dis = j.getString("discount");
//                    String im = j.getString("im").split("\\|")[0];
                    D d1 = new D();
                    d1.setQua(qua);
                    d1.setGid(gid);
                    d1.setSiz(siz);
                    d1.setNam(nam);
                    d1.setCol(col);
                    d1.setPri(pri);
                    d1.setDis(dis);
//                    d1.setIm(im);
                    status.addDd(d1);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        return status;
    }

    public static Status delete(D da){
        Status status = new Status();
        status.setRs("t");
        status.newDd();

        String id = da.getUid();
        boolean b = JudgeUid.jud(id);
        if (b == false){
            status.setRs("f");
            return status;
        }
        String uid = id.split("\\|")[0];
        String gid = da.getGid();
        String siz = da.getSiz();

        String key = uid+"_cart";
        String value = gid+"|"+siz;

        RedisOperating.hdel(key, value);

        return status;
    }

    public static Status alter(D da){
        Status status = new Status();
        status.setRs("t");
        status.newDd();

        String id = da.getUid();
        boolean b = JudgeUid.jud(id);
        if (b == false){
            status.setRs("f");
            return status;
        }
        String uid = id.split("\\|")[0];
        String cart = uid+"_cart";

        String gid = da.getGid();
        String siz = da.getSiz();
        String qua = da.getQua();
        String key = gid+"|"+siz;
        Map<String,String> map = new HashMap();
        map.put(key, qua);

        RedisOperating.hset(cart, map);

        return status;
    }

}
