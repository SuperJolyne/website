package contrual;

import pojo.Data;
import pojo.Status;

public class User {

    public static Status info(Data data){
        Data d = new Data();
        d.setNm("xxx");
        d.setPh("xxx");
        d.setBi("xxxx-xx");
        Status status = new Status();
        status.setRs("t");
        status.setD(d);
        return status;
    }

    public static Status alterInfo(Data data){
        Status status = new Status();
        status.setRs("t");
        return status;
    }

    public static Status address(Data data){
        Status status = new Status();
        status.setRs("t");
        Data d2 = new Data();
        d2.setAid("xxx");
        d2.setNm("xxxx");
        d2.setPh("xxx");
        d2.setZip("xxx");
        d2.setAd("xxxxxxxxxxxxxxx");
        Data d3 = new Data();
        d3.setAid("xxxx");
        d3.setNm("xxxx");
        d3.setPh("xxx");
        d3.setZip("xxx");
        d3.setAd("xxxxxxxxxxxxxxx");
        status.newDa();
        status.setDa(d2);
        status.setDa(d3);
        return status;
    }

    public static Status addAdd(Data data){
        Status status = new Status();
        status.setRs("t");
        return status;
    }

    public static Status deleteAdd(Data data){
        Status status = new Status();
        status.setRs("t");
        return status;
    }

    public static Status collection(Data data){
        Status status=new Status();
        if(data.getUid()!=null){
            status.setRs("t");
            Data data1=new Data();
            data1.setGid("123");
            data1.setNam("1");
            data1.setPri("2");
            data1.setDis("3");
            data1.setIm("4");
            status.newDa();
            status.setDa(data1);
        }else {
            status.setRs("f");
        }
        return status;
    }
    public static Status uncom(Data data){
        Status status=new Status();
        status.setRs("t");
        status.newDa();
        Data data1=new Data();
        data1.setOid("xxx");
        data1.setNam("xxx");
        data1.setAct("xxx");
        data1.setCol("xx");
        data1.setSiz("xx");
        data1.setIm("xxx");
        data1.setQua("xxx");
        status.setDa(data1);
        return status;
    }
    public static Status com(Data data){
        Status status=new Status();
        status.setRs("t");
        return status;
    }
    public static Status confirm(Data data){
        Status status=new Status();
        status.setRs("t");
        return status;
    }
    public static Status delectCom(Data data){
        Status status = new Status();
        if (data.getUid()!=null){
            status.setRs("t");
        }else {
            status.setRs("f");
        }
        return status;
    }
}
