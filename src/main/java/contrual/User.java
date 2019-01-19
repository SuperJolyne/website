package contrual;

import pojo.D;
import pojo.Status;

public class User {

    public static Status info(D da){
        D d = new D();
        d.setNm("xxx");
        d.setPh("xxx");
        d.setBi("xxxx-xx");
        Status status = new Status();
        status.setRs("t");
        status.setD(d);
        return status;
    }

    public static Status alterInfo(D d){
        Status status = new Status();
        status.setRs("t");
        return status;
    }

    public static Status address(D d){
        Status status = new Status();
        status.setRs("t");
        D d2 = new D();
        d2.setAid("xxx");
        d2.setNm("xxxx");
        d2.setPh("xxx");
        d2.setZip("xxx");
        d2.setAd("xxxxxxxxxxxxxxx");
        D d3 = new D();
        d3.setAid("xxxx");
        d3.setNm("xxxx");
        d3.setPh("xxx");
        d3.setZip("xxx");
        d3.setAd("xxxxxxxxxxxxxxx");
        status.newDd();
        status.addDd(d2);
        status.addDd(d3);
        return status;
    }

    public static Status addAdd(D d){
        Status status = new Status();
        status.setRs("t");
        return status;
    }

    public static Status deleteAdd(D d){
        Status status = new Status();
        status.setRs("t");
        return status;
    }

    public static Status collection(D d){
        Status status=new Status();
        if(d.getUid()!=null){
            status.setRs("t");
            D d1 =new D();
            d1.setGid("123");
            d1.setNam("1");
            d1.setPri("2");
            d1.setDis("3");
            d1.setIm("4");
            status.newDd();
            status.addDd(d1);
        }else {
            status.setRs("f");
        }
        return status;
    }
    public static Status uncom(D d){
        Status status=new Status();
        status.setRs("t");
        status.newDd();
        D d1 =new D();
        d1.setOid("xxx");
        d1.setNam("xxx");
        d1.setAct("xxx");
        d1.setCol("xx");
        d1.setSiz("xx");
        d1.setIm("xxx");
        d1.setQua("xxx");
        status.addDd(d1);
        return status;
    }
    public static Status com(D d){
        Status status=new Status();
        status.setRs("t");
        return status;
    }
    public static Status confirm(D d){
        Status status=new Status();
        status.setRs("t");
        return status;
    }
    public static Status delectCom(D d){
        Status status = new Status();
        if (d.getUid()!=null){
            status.setRs("t");
        }else {
            status.setRs("f");
        }
        return status;
    }
}
