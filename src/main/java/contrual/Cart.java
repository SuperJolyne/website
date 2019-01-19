package contrual;

import pojo.Data;
import pojo.Status;

public class Cart {

    //    private static Logger logger = Logger.getLogger(Register.class);

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

    public static Status view(Data data){
        Status status = new Status();
        status.setRs("t");
        Data d2 = new Data();
        d2.setGid("xxx");
        d2.setNam("xxx");
        d2.setCol("xxx");
        d2.setSiz("xx");
        d2.setQua("xxx");
        d2.setIm("xxxx");
        d2.setPri("xxx");
        d2.setDis("xxxx");
        Data d3 = new Data();
        d3.setGid("xxx");
        d3.setNam("xxx");
        d3.setCol("xxx");
        d3.setSiz("xx");
        d3.setQua("xxx");
        d3.setIm("xxxx");
        d3.setPri("xxx");
        d3.setDis("xxxx");
        status.newDa();
        status.setDa(d2);
        status.setDa(d3);
        return status;
    }

    public static Status delete(Data data){
        Status status=new Status();
        if(data.getUid()!=null){
            status.setRs("t");
        }
        return status;
    }


}
