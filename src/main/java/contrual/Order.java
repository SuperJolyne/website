package contrual;

import pojo.D;
import pojo.JudgeUid;
import pojo.Status;
import util.RedisOperating;

import java.util.List;

public class Order {

    public static Status myself(D da){
        Status status = new Status();
        status.setRs("t");
        //整体数据
        D d = new D();
        d.newD();

        int pag = Integer.parseInt(da.getPag());
        int start = (pag - 1)*10;
        int end = start+9;
        String id = da.getUid();
        boolean b = JudgeUid.jud(id);
        if (b == false){
            status.setRs("f");
            return status;
        }
        String uid = id.split("\\|")[0];
        String uid_order = uid+"_order";
        List<String> list = RedisOperating.lrange(uid_order,start,end);
        for (String oid : list){
            //数据中的
            D d1 = new D();
            d1.newD();

            //订单里的全部商品
            String listGoods = (String)RedisOperating.hget(oid, "H");
            //如果oid为已经失效的订单则移除
            if (null == listGoods || listGoods.equals("")){
                RedisOperating.lrem(uid_order, oid);
                continue;
            }
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

    public static Status detail(D da){
        Status status = new Status();
        status.setRs("t");

        String id = da.getUid();
        boolean b = JudgeUid.jud(id);
        if (b == false){
            status.setRs("f");
            return status;
        }

        String uid = id.split("\\|")[0];
        String oid = da.getOid();
        String aid = da.getAid();


        String wuliu = (String)RedisOperating.hget(oid, "W");
        String express = wuliu.split("\\|")[0];
        String express_id = wuliu.split("\\|")[1];

        String name = (String)RedisOperating.hget(aid, "N");
        String phone = (String)RedisOperating.hget(aid, "P");
        String address = (String)RedisOperating.hget(aid, "A");

        D d = new D();
        d.setExp(express);
        d.setExpn(express_id);
        d.setNm(name);
        d.setPh(phone);
        d.setAd(address);

        status.setD(d);
        return status;
    }

    public static Status delete(D da){
        Status status = new Status();
        status.setRs("t");

        String id = da.getUid();
        boolean b = JudgeUid.jud(id);
        if (b == false){
            status.setRs("f");
            return status;
        }

        String uid = id.split("\\|")[0];
        String oid = da.getOid();

        String key = uid+"_order";
        RedisOperating.lrem(key, oid);
        RedisOperating.del(oid);
        return status;
    }
}
