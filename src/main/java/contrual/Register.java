package contrual;

import pojo.Data;
import pojo.Status;

public class Register {

//    private static Logger logger = Logger.getLogger(Register.class);

    public static Status registers(Data data){
        Status status = new Status();
        status.setRs("t");
        Data d = new Data();
        d.setUid("1111");
        status.setD(d);
        return status;
    }

    public static Status jugde(Data data){
        Status status = new Status();
        status.setRs("t");
        Data d = new Data();
        d.setPh("1");
        d.setNm("0");
        status.setD(d);
        return status;
    }

    public static Status login(Data data){
        Status status = new Status();
        status.setRs("t");
        Data d = new Data();
        d.setUs("1");
        d.setPs("1");
        d.setSr("4");
        status.setD(d);
        return status;
    }

    public static Status forgot(Data data){
        Status status = new Status();
        status.setRs("t");
        Data d = new Data();
        d.setNm("xxx");
        d.setUid("xxx");
        status.setD(d);
        return status;
    }


}
