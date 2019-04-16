package pojo;

import util.RedisOperating;

public class JudgeUid {

    public static boolean jud(String u){
        try {
            String uid = u.split("\\|")[0];
            System.out.println(uid);
            String hex = u.split("\\|")[1];
            System.out.println(hex);
            String sign = (String)RedisOperating.get(uid+"_s");
            if (hex.equals(sign)){
                return true;
            }
            return false;
        }catch (Exception e){
            return false;
        }

    }
}
