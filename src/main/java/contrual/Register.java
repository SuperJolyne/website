package contrual;

import pojo.D;
import pojo.Status;

public class Register {

//    private static Logger logger = Logger.getLogger(Register.class);

    public static Status registers(D da){
        Status status = new Status();
        status.setRs("t");
        D d = new D();
        d.setUid("1111");
        status.setD(d);
        return status;
    }

    public static Status jugde(D da){
        Status status = new Status();
        status.setRs("t");
        D d = new D();
        d.setPh("1");
        d.setNm("0");
        status.setD(d);
        return status;
    }

    public static Status login(D da){
        Status status = new Status();
        status.setRs("t");
        D d = new D();
        d.setUs("1");
        d.setPs("1");
        d.setSr("4");
        status.setD(d);
        return status;
    }

    public static Status forgot(D da){
        Status status = new Status();
        status.setRs("t");
        D d = new D();
        d.setNm("xxx");
        d.setUid("xxx");
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
