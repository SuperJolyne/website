package contrual;

import pojo.D;
import pojo.JudgeUid;
import pojo.Status;
import util.RedisOperating;

import java.util.List;

public class Collection {
    public static Status delete(D da){
        Status status = new Status();
        status.setRs("t");

        String id = da.getUid();
        boolean b = JudgeUid.jud(id);
        if (b == false){
            status.setRs("f");
            return status;
        }

        String uid = id.split("\\|")[0];
        String gid = da.getGid();
        String key = uid+"_college";

        RedisOperating.lrem(key, gid);

        return status;
    }


    public static Status judge(D da){
        Status status = new Status();
        status.setRs("t");

        String id = da.getUid();
        boolean b = JudgeUid.jud(id);
        if (b == false){
            status.setRs("f");
            return status;
        }

        String uid = id.split("\\|")[0];
        String key = uid+"_college";
        String gid = da.getGid();

        List<String> list = RedisOperating.lrange(key, 0, 100);
        D d = new D();
        d.setIs("0");
        for (String s : list){
            if (s.equals(gid)){
                d.setIs("1");
            }
        }
        status.setD(d);
        return status;
    }
}
