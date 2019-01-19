package contrual;

import pojo.D;
import pojo.Status;

public class Cart {

    //    private static Logger logger = Logger.getLogger(Register.class);

    public static Status view(D d){
        Status status = new Status();
        status.setRs("t");
        D d2 = new D();
        d2.setGid("xxx");
        d2.setNam("xxx");
        d2.setCol("xxx");
        d2.setSiz("xx");
        d2.setQua("xxx");
        d2.setIm("xxxx");
        d2.setPri("xxx");
        d2.setDis("xxxx");
        D d3 = new D();
        d3.setGid("xxx");
        d3.setNam("xxx");
        d3.setCol("xxx");
        d3.setSiz("xx");
        d3.setQua("xxx");
        d3.setIm("xxxx");
        d3.setPri("xxx");
        d3.setDis("xxxx");
        status.newDd();
        status.addDd(d2);
        status.addDd(d3);
        return status;
    }

    public static Status delete(D d){
        Status status=new Status();
        if(d.getUid()!=null){
            status.setRs("t");
        }
        return status;
    }


}
