package contrual;

import pojo.D;
import pojo.Status;

public class Goods {

    public static Status search(D d){
        Status status = new Status();
        status.setRs("t");
        D d2 = new D();
        d2.setGid("xxx");
        d2.setNam("xxx");
        d2.setPri("xxx");
        d2.setDis("xxx");
        d2.setIm1("xxxxxxxxxxxxxx");
        d2.setIm2("xxxxxxxxxxxxxxxx");
        D d3 = new D();
        d3.setGid("xxxx");
        d3.setNam("xxx");
        d3.setPri("xxx");
        d3.setDis("xxx");
        d3.setIm1("xxxxxxxxxxxxxx");
        d3.setIm2("xxxxxxxxxxxxxxxx");
        status.newDd();
        status.addDd(d2);
        status.addDd(d3);
        return status;
    }

    public static Status searchCtn(D d){
        Status status = new Status();
        status.setRs("t");
        D d2 = new D();
        d2.setGid("xxx");
        d2.setNam("xxx");
        d2.setPri("xxx");
        d2.setDis("xxx");
        d2.setIm1("xxxxxxxxxxxxxx");
        d2.setIm2("xxxxxxxxxxxxxxxx");
        D d3 = new D();
        d3.setGid("xxxx");
        d3.setNam("xxx");
        d3.setPri("xxx");
        d3.setDis("xxx");
        d3.setIm1("xxxxxxxxxxxxxx");
        d3.setIm2("xxxxxxxxxxxxxxxx");
        status.newDd();
        status.addDd(d2);
        status.addDd(d3);
        return status;
    }

    public static Status cart(D d){
        System.out.println(d.getUid());
        Status status = new Status();
        if(d.getUid()!=null){
            status.setRs("t");
        }else {
            status.setRs("f");
        }
        return status;
    }
    public static Status collection(D d){
        System.out.println(d.getUid());
        Status status =new Status();
        if(d.getUid()!=null&& d.getGid()!=null){
            status.setRs("t");
        }else {
            status.setRs("f");
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
    public static Status recomm(D d){
        Status status=new Status();
        if(d.getNam()!=null){
            status.setRs("t");
            status.newDd();
            D d1 =new D();
            d1.setGid("xx");
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
}
