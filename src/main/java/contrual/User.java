package contrual;

import Osscli.App;
import com.alibaba.fastjson.JSONObject;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.model.OSSObject;
import pojo.D;
import pojo.JudgeUid;
import pojo.Status;
import util.ManageCid;
import util.RedisOperating;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class User {

    public static Status info(D da){
        Status status = new Status();
        String uid = da.getUid();
        boolean b = JudgeUid.jud(uid);
        if (b == false){
            status.setRs("f");
            return status;
        }
        String id = uid.split("\\|")[0];
        Map<String, String> map = RedisOperating.hgetAll(id);
        D d = new D();
        d.setNm(map.get("N"));
        d.setPh(map.get("P"));
        d.setBi(map.get("B"));
        status.setRs("t");
        status.setD(d);
        return status;
    }

    public static Status alterInfo(D da){
        Status status = new Status();
        String uid = da.getUid();
        boolean b = JudgeUid.jud(uid);
        if (b == false){
            status.setRs("f");
            return status;
        }
        String id = uid.split("\\|")[0];
        String ph = da.getPh();
        String bi = da.getBi();
        String cd = da.getCd();

        D d = new D();
        if (ph == null){
            //ph为空，bi必然不为空
            Map<String, String> map = new HashMap<>();
            map.put("B", bi);
            RedisOperating.hset(id, map);
        }else {
            String oldPh = (String)RedisOperating.hget(id, "P");
            String pw_uid = (String)RedisOperating.get(oldPh);
            String code = (String)RedisOperating.get(ph+"_ttl");
            if (cd.equals(code)) {
                Map<String, String> map = new HashMap<>();
                map.put("P", ph);
                if (bi != null) {
                    map.put("B", bi);
                }
                RedisOperating.hset(uid, map);//更改用户信息的手机号
                RedisOperating.expire(ph + "_ttl", 0);
                RedisOperating.set(ph, pw_uid);//更改用户登录的手机号
                RedisOperating.del(oldPh);
            }else {
                d.setCs("f");
                return status;
            }
        }

        status.setRs("t");
        return status;
    }

    public static Status address(D da){
        Status status = new Status();
        status.setRs("t");
        status.newDd();

        String uid = da.getUid();
        boolean b = JudgeUid.jud(uid);
        if (b == false){
            status.setRs("f");
            return status;
        }
        String id = uid.split("\\|")[0];

        //地址只能有10个
        List<String> list = RedisOperating.lrange(id+"_address",0,10);

        for (String listid: list){
            Map<String, String> map = RedisOperating.hgetAll(listid);
            D d = new D();
            d.setAid(listid);
            d.setNm(map.get("N"));
            d.setPh(map.get("P"));
            d.setZip(map.get("Z"));
            d.setAd(map.get("A"));
            status.addDd(d);
        }

        return status;
    }

    public static Status addAdd(D da){
        Status status = new Status();
        status.setRs("t");

        String uid = da.getUid();
        boolean b = JudgeUid.jud(uid);
        if (b == false){
            status.setRs("f");
            return status;
        }
        String id = uid.split("\\|")[0];
        int num = (int) RedisOperating.incr("createadd");
        String aid = "add:"+String.format("%08d",num);
        Map<String, String> map = new HashMap<>();
        map.put("N", da.getNm());
        map.put("P", da.getPh());
        map.put("Z", da.getZip());
        map.put("A", da.getAd());
        RedisOperating.hset(aid, map);
        RedisOperating.lpus(id+"_address",aid);
        return status;
    }

    public static Status deleteAdd(D da){
        Status status = new Status();
        status.setRs("t");

        String id = da.getUid();
        boolean b = JudgeUid.jud(id);
        if (b == false){
            status.setRs("f");
            return status;
        }
        String uid = id.split("\\|")[0];
        String aid = da.getAid();
        String key = uid+"_address";
        status.newDd();

        //删除列表中的aid
        RedisOperating.lrem(key, aid);
        RedisOperating.del(aid);

        return status;
    }

    public static Status collection(D da){
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
        String key = uid+"_college";
        int pag = Integer.parseInt(da.getPag());
        int from = (pag-1)*20;
        int to = from+20;

        List<String> list = RedisOperating.lrange(key, from, to);
        for (String s : list){
            D d = new D();
            d.setGid(s);
            status.addDd(d);
        }

        return status;
    }

    public static Status uncom(D da){
        Status status = new Status();
        status.setRs("t");
        //整体数据
        D d = new D();
        d.newD();

//        int pag = Integer.parseInt(da.getPag());
//        int start = (pag - 1)*10;
//        int end = start+9;
        String id = da.getUid();
        boolean b = JudgeUid.jud(id);
        if (b == false){
            status.setRs("f");
            return status;
        }
        String uid = id.split("\\|")[0];
//        String uid_order = uid+"_order";
//        List<String> list = RedisOperating.lrange(uid_order,start,end);
        String v = (String)RedisOperating.hget("comment", uid);
        String list_oid[] = v.split("\\|");
        for (String oid : list_oid){

            D d1 = new D();
            d1.newD();

            //订单里的全部商品
            String listGoods = (String)RedisOperating.hget(oid, "H");
            String goods[] = listGoods.split("_");
            for (String s:goods){
                D d2 = new D();
                String gid = s.split("\\|")[0];
                String size = s.split("\\|")[1];
                String qua = s.split("\\|")[2];
                String price = s.split("\\|")[3];
                d2.setGid(gid);
                d2.setSiz(size);
                d2.setQua(qua);
                d2.setPri(price);
                d1.addDa(d2);
            }

            String aid = (String)RedisOperating.hget(oid, "A");
            String money = (String)RedisOperating.hget(oid, "M");
            String pri = money.split("\\|")[0];
            String autcally = money.split("\\|")[1];
            String discount = money.split("\\|")[2];

            String coupon = (String)RedisOperating.hget(oid, "Y");
            //查看订单列表不需要查看物流信息
//            String wuliu = (String)RedisOperating.hget(oid, "W");
//            String express = wuliu.split("\\|")[0];
//            String express_id = wuliu.split("\\|")[1];
            String order_status = (String)RedisOperating.hget(oid, "S");
            String time = (String)RedisOperating.hget(oid, "T");

            d1.setOid(oid);
            d1.setAid(aid);
            d1.setPri(pri);
            d1.setAct(autcally);
            d1.setDis(discount);
            d1.setSta(order_status);
            d1.setTim(time);
//            d1.setExp(express);
//            d1.setExpn(express_id);

            d.addDa(d1);

        }

        status.setD(d);
        return status;
    }

    public static Status com(D da) throws IOException {
        Status status = new Status();
        status.setRs("t");

        String id = da.getUid();
        boolean b = JudgeUid.jud(id);
        if (b == false){
            status.setRs("f");
            return status;
        }

        String uid = id.split("\\|")[0];
        String nm = (String)RedisOperating.hget(uid, "N");
        String oid = da.getOid();
        List<D> dList = da.getDa();

        for (D dd:dList) {
            String gid = dd.getGid();

            String type = "my0039/page/comm/myself/";

            long createcom = RedisOperating.incr("createcom");
            String format = String.format("%08d", createcom);
            String cid = "com:" + format;
            System.out.println("cid:" + cid);
            //用户自身的
            ManageCid.doAdd(dd, uid, type, cid, nm, oid);

            //商品
            type = "my0039/page/comm/goods/";
            ManageCid.doAdd(dd, gid, type, cid, nm, oid);


        }
        //把用户未评论的oid删除
        String v = (String)RedisOperating.hget("comment", uid);
        String list_oid[] = v.split("\\|");
        List<String> list = new ArrayList<>();
        for (String s : list_oid){
            list.add(s);
        }
        list.remove(oid);
        String new_oid = "";
        for (String s : list){
            new_oid = s+"|";
        }
        Map<String , String > map = new HashMap<>();
        map.put(uid, new_oid);
        RedisOperating.hset("comment", map);

        return status;

    }

    public static Status confirm(D d){
        Status status=new Status();
        status.setRs("t");
        return status;
    }

    public static Status delectCom(D da) throws IOException {
        Status status = new Status();
        status.setRs("t");

        String id = da.getUid();
        boolean b = JudgeUid.jud(id);
        if (b == false) {
            status.setRs("f");
            return status;
        }

        String uid = id.split("\\|")[0];
        String cid = da.getCid();
        String gid = "";

        OSSClient ossClient = App.getOSS();
        String bucketName = "superljc";
        String type = "my0039/page/comm/myself/";
        String cid_add = type + cid + ".json";

        OSSObject cid_Object = ossClient.getObject(bucketName, cid_add);

        // 读取文件内容。
        String cid_String = "";
        BufferedReader cid_reader = new BufferedReader(new InputStreamReader(cid_Object.getObjectContent()));
        while (true) {
            String line = cid_reader.readLine();
            if (line == null) {
                break;
            } else {
                cid_String += line;
            }
        }
        // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
        cid_reader.close();
        JSONObject cid_json = JSONObject.parseObject(cid_String);
        String fr = cid_json.getString("fr");
        String nx = cid_json.getString("nx");
        JSONObject ctn = cid_json.getJSONObject("ctn");
        gid = ctn.getString("gid");//获取评价中的gid
        //删除用户的
        ManageCid.doDelete(cid_add, fr, nx, uid, type, cid);

        //对商品评论进行处理
        type = "my0039/page/comm/goods/";
        cid_add = type + cid + ".json";

        cid_Object = ossClient.getObject(bucketName, cid_add);

        // 读取文件内容。
        cid_String = "";
        cid_reader = new BufferedReader(new InputStreamReader(cid_Object.getObjectContent()));
        while (true) {
            String line = cid_reader.readLine();
            if (line == null) {
                break;
            } else {
                cid_String += line;
            }
        }
        // 数据读取完成后，获取的流必须关闭，否则会造成连接泄漏，导致请求无连接可用，程序无法正常工作。
        cid_reader.close();
        cid_json = JSONObject.parseObject(cid_String);
        fr = cid_json.getString("fr");
        nx = cid_json.getString("nx");
        //删除商品的
        ManageCid.doDelete(cid_add, fr, nx, gid, type, cid);

        return status;
    }

    public static Status coupon(D da){
        Status status = new Status();
        status.setRs("t");

        String id = da.getUid();
        boolean b = JudgeUid.jud(id);
        if (b == false){
            status.setRs("f");
            return status;
        }

        String uid = id.split("\\|")[0];
        //拿出用户下所有的优惠券id
        String did_String = (String) RedisOperating.hget(uid, "C");
        status.newDd();
        String did_list[] = did_String.split("\\|");
        for (String did : did_list){
            //拿出优惠券的信息
            String did_info = (String) RedisOperating.get(did);
            JSONObject json = JSONObject.parseObject(did_info);
            String nm = json.getString("nm");
            String dis = json.getString("dis");
            String dea = json.getString("dea");

            D d = new D();
            d.setDid(did);
            d.setNm(nm);
            d.setDis(dis);
            d.setDea(dea);

            status.addDd(d);
        }

        return status;
    }
}
