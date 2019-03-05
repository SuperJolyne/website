package contrual;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import elas.SearchDocument;
import pojo.D;
import pojo.JudgeUid;
import pojo.Status;
import util.RedisOperating;

import java.io.IOException;
import java.util.*;

public class Goods {

    public static Status search(D da) throws IOException {
        Status status = new Status();
        status.setRs("t");
        status.newDd();

        String money = da.getMon();

        int gte = Integer.parseInt(money.split("\\-")[0]);
        int lte = Integer.parseInt(money.split("\\-")[1]);

        elas.Goods goods = new elas.Goods();
        goods.setOne(da.getOne());
        goods.setTwo(da.getTwo());
        goods.setThree(da.getThr());
        goods.setSize(da.getSiz());
        goods.setColor(da.getCol());
        goods.setBrand(da.getLo());

        String fields = JSON.toJSONString(goods);

        int pag = Integer.parseInt(da.getPag());
        int from = (pag-1)*40;
        int pagsize = 40;

        //es搜索
        List<String> jsonList = SearchDocument.groupSearch("my00391","goods",fields,from,gte,lte,pagsize);
        for (String s : jsonList){
            D d = new D();
            JSONObject j = JSONObject.parseObject(s);
            d.setGid(j.getString("gid"));
            d.setNam(j.getString("title"));
            d.setPri(j.getString("price"));
            d.setDis(j.getString("discount"));
            String im[] = j.getString("im").split("\\|");
            d.setIm1(im[0]);
            if (im.length>1) {
                d.setIm2(im[1]);
            }
            status.addDd(d);
        }

        return status;
    }

    public static Status searchCtn(D da) throws IOException {
        Status status = new Status();
        status.setRs("t");
        status.newDd();

        elas.Goods goods = new elas.Goods();
        String one = da.getOne();
        String two = da.getTwo();
        String three = da.getThr();
        String size = da.getSiz();
        String color = da.getCol();
        String logo = da.getLo();
        String ctn = da.getCtn();

        String fenci_list[] = ctn.split(" ");
        String fenci_key = "fenci";
        for (String fenci : fenci_list){
            RedisOperating.zincrby(fenci_key, fenci);
        }




        String fields = (one+" "+two+" "+three+" "+size+" "+color+" "+logo+" "+ctn).replaceAll("null", "");
        System.out.println(fields);

        String money = da.getMon();

        int gte = Integer.parseInt(money.split("\\-")[0]);
        int lte = Integer.parseInt(money.split("\\-")[1]);

        int pag = Integer.parseInt(da.getPag());
        int from = (pag-1)*40;
        int pagesize = 40;

        List<String> jsonList = SearchDocument.esSearch("my00391","goods",fields,from,gte,lte,pagesize);
        for (String s : jsonList){
            D d = new D();
            JSONObject j = JSONObject.parseObject(s);
            d.setGid(j.getString("gid"));
            d.setNam(j.getString("title"));
            d.setPri(j.getString("price"));
            d.setDis(j.getString("discount"));
            String im[] = j.getString("im").split("\\|");
            d.setIm1(im[0]);
            if (im.length>1) {
                d.setIm2(im[1]);
            }
            status.addDd(d);
        }

        return status;
    }

    public static Status cart(D da){
        Status status = new Status();
        status.setRs("t");

        String id = da.getUid();
        boolean b = JudgeUid.jud(id);
        if (b == false){
            status.setRs("f");
            return status;
        }

        String uid = id.split("\\|")[0];
        String gid = da.getGid();
        String col = da.getCol();
        String siz = da.getSiz();
        String qua = da.getQua();

        String cart = uid+"_cart";

        String key = gid+"|"+siz;
        Map<String , String> map = new HashMap<>();
        map.put(key, qua);

        D d = new D();
        long l = RedisOperating.hlen(cart);
        if (l<=50){
            RedisOperating.hset(cart, map);
            d.setJ("1");
            status.setD(d);
        }else {
            d.setJ("0");
            status.setD(d);
        }

        return status;
    }

    public static Status collection(D da){
        Status status = new Status();
        status.setRs("t");

        String id = da.getUid();
        boolean b = JudgeUid.jud(id);
        if (b == false){
            status.setRs("f");
            return status;
        }

        String uid = id.split("\\|")[0];
        String gid = da.getGid();

        String key = uid+"_college";

        long l = RedisOperating.llen(key);

        D d = new D();
        if (l<=100){
            RedisOperating.lpus(key,gid);
            d.setJ("1");
            status.setD(d);
        }else {
            d.setJ("0");
            status.setD(d);
        }

        return status;
    }

    public static Status custom(D d){
        Status status=new Status();
        if(d.getCtn()!=null){
            status.setRs("t");
            status.newDd();
            D d1 =new D();
            d1.setGid("x");
            d1.setNam("xxx");
            d1.setPri("xxx");
            d1.setDis("xxx");
            d1.setIm1("xx");
            d1.setIm2("xx");
            status.addDd(d1);
        }else {
            status.setRs("f");
        }
        return status;
    }

    public static Status recomm(D da) throws IOException {
        Status status=new Status();
        status.newDd();

        String name = da.getNam();
        int from = 0;
        int gte = 0;
        int lte = 30000;
        //只是为了获取商品的类别
        int size = 1;
        List<String> jsonList = SearchDocument.groupSearch("my00391","goods",name,from,gte,lte,size);
        String one = "";
        String two = "";
        String three = "";
        for (String s : jsonList){
            JSONObject j = JSONObject.parseObject(s);
            one = j.getString("one");
            two = j.getString("two");
            three = j.getString("three");
        }

        //获取上面类别的所有商品
        size = 10000;
        String fields = one+" "+two+" "+three;
        List<String> list = SearchDocument.groupSearch("my00391","goods",fields,from,gte,lte,size);
        int random = list.size();
        Set<Integer> set = new HashSet<>();
        //随机选出四个商品
        while (set.size()<4){
            set.add(new Random().nextInt(random));
        }
        for (Integer i : set){
            D d = new D();
            JSONObject j = JSONObject.parseObject(list.get(i));
            d.setGid(j.getString("gid"));
            d.setNam(j.getString("title"));
            d.setPri(j.getString("price"));
            d.setDis(j.getString("discount"));
            String im[] = j.getString("im").split("\\|");
            d.setIm1(im[0]);
            if (im.length>1) {
                d.setIm2(im[1]);
            }
            status.addDd(d);
        }

        status.setRs("t");
        return status;
    }
}
