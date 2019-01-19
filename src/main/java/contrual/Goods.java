package contrual;

import pojo.Data;
import pojo.Status;

public class Goods {

    public static Status search(Data data){
        Status status = new Status();
        status.setRs("t");
        Data d2 = new Data();
        d2.setGid("xxx");
        d2.setNam("xxx");
        d2.setPri("xxx");
        d2.setDis("xxx");
        d2.setIm1("xxxxxxxxxxxxxx");
        d2.setIm2("xxxxxxxxxxxxxxxx");
        Data d3 = new Data();
        d3.setGid("xxxx");
        d3.setNam("xxx");
        d3.setPri("xxx");
        d3.setDis("xxx");
        d3.setIm1("xxxxxxxxxxxxxx");
        d3.setIm2("xxxxxxxxxxxxxxxx");
        status.newDa();
        status.setDa(d2);
        status.setDa(d3);
        return status;
    }

    public static Status searchCtn(Data data){
        Status status = new Status();
        status.setRs("t");
        Data d2 = new Data();
        d2.setGid("xxx");
        d2.setNam("xxx");
        d2.setPri("xxx");
        d2.setDis("xxx");
        d2.setIm1("xxxxxxxxxxxxxx");
        d2.setIm2("xxxxxxxxxxxxxxxx");
        Data d3 = new Data();
        d3.setGid("xxxx");
        d3.setNam("xxx");
        d3.setPri("xxx");
        d3.setDis("xxx");
        d3.setIm1("xxxxxxxxxxxxxx");
        d3.setIm2("xxxxxxxxxxxxxxxx");
        status.newDa();
        status.setDa(d2);
        status.setDa(d3);
        return status;
    }

    public static Status cart(Data data){
        System.out.println(data.getUid());
        Status status = new Status();
        if(data.getUid()!=null){
            status.setRs("t");
        }else {
            status.setRs("f");
        }
        return status;
    }
    public static Status collection(Data data){
        System.out.println(data.getUid());
        Status status =new Status();
        if(data.getUid()!=null&&data.getGid()!=null){
            status.setRs("t");
        }else {
            status.setRs("f");
        }
        return status;
    }
    public static Status custom(Data data){
        Status status=new Status();
        if(data.getCtn()!=null){
            status.setRs("t");
            status.newDa();
            Data data1=new Data();
            data1.setGid("x");
            data1.setNam("xxx");
            data1.setPri("xxx");
            data1.setDis("xxx");
            data1.setIm1("xx");
            data1.setIm2("xx");
            status.setDa(data1);
        }else {
            status.setRs("f");
        }
        return status;
    }
    public static Status recomm(Data data){
        Status status=new Status();
        if(data.getNam()!=null){
            status.setRs("t");
            status.newDa();
            Data data1=new Data();
            data1.setGid("xx");
            data1.setNam("xxx");
            data1.setPri("xxx");
            data1.setDis("xxx");
            data1.setIm1("xx");
            data1.setIm2("xx");
            status.setDa(data1);
        }else {
            status.setRs("f");
        }
        return status;
    }
}
