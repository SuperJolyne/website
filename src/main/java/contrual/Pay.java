package contrual;

import com.alibaba.fastjson.JSONObject;
import pojo.D;
import pojo.JudgeUid;
import pojo.Status;
import util.RedisOperating;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pay {

    public static Status create(D da) throws IOException { //还差添加支付订单号
        Status status = new Status();
        status.setRs("t");
        D d1 = new D();

        String id = da.getUid();
        boolean b = JudgeUid.jud(id);
        if (b == false){
            status.setRs("f");
            return status;
        }

        String uid = id.split("\\|")[0];
        List<D> d_list = da.getDa();
        String list = "";
        for (D d : d_list){
            String gid = d.getGid();
            String siz = d.getSiz();
            String qua = d.getQua();
            String pri = d.getPri();
            String s = gid+"|"+siz+"|"+qua+"|"+pri;
            list = list + s + "_";
        }

        String aid = da.getAid();
        String did = da.getDid();
        String pri = da.getPri();
        String act = da.getAct();
        String di = da.getDi();
        String money = pri+"|"+act+"|"+di;

        //删除优惠券
        String did_Str = (String) RedisOperating.hget(uid, "C");
        String did_list[] = did_Str.split("\\|");
        String new_did = "";
        for (String s : did_list){
            if (s.equals(did)){
                continue;
            }
            new_did += s;
        }
        Map<String, String> did_map = new HashMap<>();
        did_map.put("C", new_did);
        RedisOperating.hset(uid, did_map);

        long ti = System.currentTimeMillis();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        String now = df.format(new Date(ti));

        Map<String, String> map = new HashMap<>();
        map.put("H", list);
        map.put("A", aid);
        map.put("M", money);
        map.put("U", uid);
        map.put("S", "未支付");
        map.put("T", now);
        map.put("D", did);

        String oid = now+uid;
        RedisOperating.hset(oid, map);//将订单存入redis中
        RedisOperating.expire(oid, 60*30);//设置订单过期时间，30分钟

        String key = uid+"_order";
        RedisOperating.lpus(key,oid);

        //写到日志系统
        File log_f = new File("/home/super/Business/log/wfk/wfk.log");
        RandomAccessFile log_raf = new RandomAccessFile(log_f, "rw");
        log_raf.seek(log_f.length());
        JSONObject log_json = new JSONObject();
        log_json.put("uid", uid);
        log_json.put("oid", oid);
        log_json.put("gid_size_qua", list);
        log_json.put("status", "未支付");
        log_json.put("pri", pri);
        log_json.put("act", act);
        log_json.put("dis", di);
        log_json.put("did", did);//把数据库表中couponid改为did
        log_json.put("time", ti);
        log_json.put("order_time", now);
        log_raf.write((log_json.toJSONString()+"\n").getBytes());
        log_raf.close();

        d1.setOid(oid);
        status.setD(d1);
        return status;
    }

    public static Status cancel(D da) throws IOException {
        Status status = new Status();
        status.setRs("t");
        D d1 = new D();

        String id = da.getUid();
        boolean b = JudgeUid.jud(id);
        if (b == false){
            status.setRs("f");
            return status;
        }

        String uid = id.split("\\|")[0];
        String oid = da.getOid();

        String oid_did = (String) RedisOperating.hget(oid, "D");//订单中的did
        String uid_did = (String) RedisOperating.hget(uid, "C");//用户信息中的did
        uid_did = uid_did+"|"+oid_did;
        Map<String, String> map = new HashMap<>();
        map.put("C", uid_did);
        RedisOperating.hset(uid, map);//将优惠券重新加入到用户信息中

        String uid_order = uid+"_order";
        RedisOperating.lrem(uid_order, oid);//从用户订单中删除该订单，因为oid还有30分钟ttl，所以不用删除oid

        //写入日志系统
        File log_f = new File("/home/super/Business/log/qx/qx.log");
        RandomAccessFile log_raf = new RandomAccessFile(log_f, "rw");
        log_raf.seek(log_f.length());
        JSONObject log_json = new JSONObject();
        log_json.put("oid", oid);
        log_json.put("status", "已取消");
        long time = System.currentTimeMillis();
        log_json.put("time", time);
        log_raf.write(log_json.toJSONString().getBytes());
        log_raf.close();

        return status;
    }

    public static Status refund(D da) throws IOException {
        Status status = new Status();
        status.setRs("t");
        D d1 = new D();

        String id = da.getUid();
        boolean b = JudgeUid.jud(id);
        if (b == false){
            status.setRs("f");
            return status;
        }

        String uid = id.split("\\|")[0];
        String oid = da.getOid();

        String oid_did = (String) RedisOperating.hget(oid, "D");//订单中的did
        String uid_did = (String) RedisOperating.hget(uid, "C");//用户信息中的did
        String money = (String) RedisOperating.hget(oid, "M");
        String act = money.split("\\|")[1];//用户实际付款金额
        /**
         * 这里补充退款用户的实际付款金额
         */
        uid_did = uid_did+"|"+oid_did;
        Map<String, String> map = new HashMap<>();
        map.put("C", uid_did);
        RedisOperating.hset(uid, map);//将优惠券重新加入到用户信息中

        //写入日志系统
        File log_f = new File("/home/super/Business/log/tk/tk.log");
        RandomAccessFile log_raf = new RandomAccessFile(log_f, "rw");
        log_raf.seek(log_f.length());
        JSONObject log_json = new JSONObject();
        log_json.put("oid", oid);
        log_json.put("status", "已退款");
        long time = System.currentTimeMillis();
        log_json.put("time", time);
        log_raf.write(log_json.toJSONString().getBytes());
        log_raf.close();

        return status;
    }
}
