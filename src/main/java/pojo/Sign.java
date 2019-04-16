package pojo;

import java.util.UUID;

public class Sign {

    public static String sign(){
        UUID u = UUID.randomUUID();
        String hex = Integer.toHexString(u.hashCode());
        return hex;
    }
}
