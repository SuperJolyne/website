package contrual;

import pojo.Data;
import pojo.Status;

public class Order {

    public static Status myself(Data data){
        Status status = new Status();
        status.setRs("t");
        Data d2 = new Data();
        d2.setOid("xxx");
        d2.setAct("xxx");
        d2.setSta("xxx");

        Data d4 = new Data();
        d4.setGid("xxx");
        d4.setNam("xxxx");
        d4.setSiz("xxxx");
        d4.setQua("xxxx");
        d4.setIm("xxxx");
        d4.setPri("xxxx");

        d2.newD();
        d2.setD(d4);

        status.setD(d2);
        return status;
    }

    public static Status detail(Data data){
        Status status = new Status();
        status.setRs("t");
        Data d2 = new Data();
        d2.setPri("xx");
        d2.setDis("xxx");
        d2.setCou("xxx");
        d2.setAct("xxxx");
        d2.setSta("xxxxx");
        d2.setLog("xxx");
        d2.setNm("xxxx");
        d2.setPh("xxxx");
        d2.setAd("xxxx");
        status.setD(d2);
        return status;
    }

    public static Status delete(Data data){
        Status status = new Status();
        status.setRs("t");
        return status;
    }
}
