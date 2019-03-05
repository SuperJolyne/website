package contrual;

import com.alibaba.fastjson.JSON;
import pojo.D;
import pojo.Okle;
import pojo.Sign;
import pojo.Status;
import sun.nio.ch.Secrets;
import util.RedisOperating;
import util.RedisUtils;

import javax.crypto.SecretKey;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Register {

//    private static Logger logger = Logger.getLogger(Register.class);

    public static Status registers(D da) throws IOException {
        Status status = new Status();
        status.setRs("t");
        D d = new D();
        String uid = "";
        String ph = da.getPh();
        Object s = RedisOperating.get(ph);
        if (s != null){
            d.setPh("0");
            status.setD(d);
            return status;
        }else {
            String code = (String) RedisOperating.get(da.getPh() + "_ttl");
            if (code == null ||!code.equals(da.getCd())) {
                d.setCs("f");
                status.setD(d);
                return status;
            } else {
                Map<String, String> map = new HashMap<>();
                map.put("N", da.getNm());
                map.put("P", da.getPh());
                map.put("PW", da.getPw());
                map.put("M", da.getMl());
                map.put("C", "cou:00000001");//优惠券
                map.put("B", da.getBi());
                map.put("S", da.getSx());
                int num = (int) RedisOperating.incr("createuse");
                String id = String.format("%08d",num);
                uid = "user:" + id;
                RedisOperating.hset(uid, map);
                RedisOperating.expire(da.getPh() + "_ttl", 0);
                String sign = Sign.sign();//添加签名验证
                uid = uid+"|"+sign;
                RedisOperating.set(ph,da.getPw()+"|"+uid);
                RedisOperating.set(uid+"_s", sign);

                //写入文件系统
                RandomAccessFile f = new RandomAccessFile(Okle.userLog, "rw");
                long length = f.length();
                f.seek(length);
                long time = System.currentTimeMillis();
                Date now = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                String dstr = sdf.format(now);
                String str = uid+","+da.getNm()+","+da.getSx()+","+da.getPh()+","+
                        da.getMl()+","+da.getPw()+"cou:00000001"+","+dstr+"    "+time;
                f.write(str.getBytes());
                f.write("\n".getBytes());
                f.close();
            }
            d.setUid(uid);
            d.setCs("t");
        }
        status.setD(d);
        return status;
    }

    public static Status jugde(D da){
        String ph = da.getPh();
        String nm = da.getNm();

        D d = new D();
        if (ph != null||"".equals(ph)) {
            Object s = RedisOperating.get(ph);
            if (s == null){
                d.setPh("1");
            }else {
                d.setPh("0");
            }
        }
        if (nm != null||"".equals(nm)){
            Object s1 = RedisOperating.get(nm);
            if (s1 == null){
                d.setNm("1");
            }else {
                d.setNm("0");
            }
        }

        Status status = new Status();
        status.setRs("t");
        status.setD(d);
        return status;
    }

    public static Status login(D da){
        String ph = da.getPh();
        String pw = da.getPw();

        Status status = new Status();
        status.setRs("t");
        D d = new D();

        String value = (String)RedisOperating.get(ph);
        //账号不存在
        if (value == null){
            d.setUs("0");
            status.setD(d);
            return status;
        }

        if (RedisOperating.get(ph+"_times").equals("0")){
            d.setSr("0");
            d.setPs("0");
            status.setD(d);
            return status;
        }
        if(value.split("\\|")[0].equals(pw)){
            d.setPs("1");
            d.setUid(value.split("\\|")[1]);
        }else {
            if(RedisOperating.get(ph+"_times") == null){
                RedisOperating.set(ph+"_times","5");
                d.setSr("5");
                d.setPs("0");
                status.setD(d);
                return status;
            }else{
                long l = RedisOperating.decr(ph+"_times");
                d.setSr(String.valueOf(l));
                d.setPs("0");
            }
        }

        status.setD(d);
        return status;
    }

    public static Status forgot(D da){
        String ph = da.getPh();
        String cd = da.getCd();
        String pw = da.getPw();
        D d = new D();
        Status status = new Status();
        status.setRs("t");
        String nm = "";

        String value = (String)RedisOperating.get(ph);
        String uid = value.split("\\|")[1];
        //账号不存在
        if (value == null){
            d.setUs("0");
            status.setD(d);
            return status;
        }

        if (!RedisOperating.get(ph+"_ttl").equals(cd)){
            status.setRs("f");
            return status;
        }else {
            //如果验证码正确，把redis里的用户密码更改过来
            value = pw+"|"+uid;
            RedisOperating.set(ph,value);
            Map<String , String> map = new HashMap<>();
            map.put("PW", pw);
            RedisOperating.hset(uid,map);
            nm = (String)RedisOperating.hget(uid,"N");
        }

        d.setNm(nm);
        d.setUid(uid);
        status.setD(d);
        return status;
    }

    public static Status code(D da){
        Status status = new Status();
        status.setRs("t");
        D d = new D();
        d.setCod("xxxxx");
        return status;
    }
}
