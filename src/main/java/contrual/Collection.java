package contrual;

import pojo.Data;
import pojo.Status;

public class Collection {
    public static Status delete(Data data){
        Status status=new Status();
        if(data.getUid()!=null){
            status.setRs("t");
        }
        return status;
    }
    public static Status judge(Data data){
        Status status=new Status();
        if(data.getUid()!=null){
            status.setRs("t");
            Data data1=new Data();
            data1.setIs("1");
            status.setD(data1);
        }
        return status;
    }
}
