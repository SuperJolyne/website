package contrual;

import pojo.D;
import pojo.Status;

public class Collection {
    public static Status delete(D d){
        Status status=new Status();
        if(d.getUid()!=null){
            status.setRs("t");
        }
        return status;
    }
    public static Status judge(D d){
        Status status=new Status();
        if(d.getUid()!=null){
            status.setRs("t");
            D d1 =new D();
            d1.setIs("1");
            status.setD(d1);
        }
        return status;
    }
}
