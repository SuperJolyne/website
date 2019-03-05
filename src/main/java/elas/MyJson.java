package elas;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author jijngbao
 * @date 19-1-23
 */
public class MyJson {
    public String toJson(Class c) throws ClassNotFoundException,
            IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        StringBuffer sb=new StringBuffer("{");
        int flag=0;
//        Class c = Class.forName(this.getClass().getName());
        Field[] fields = c.getDeclaredFields();
        for (Field f : fields) {
            String propname = f.getName();
            String methodname = "get" +
                    propname.substring(0, 1).toUpperCase()
                    + propname.substring(1, propname.length());
            Method method = c.getMethod(methodname);
            Object temp= method.invoke(this);
            if (temp!=null&&!temp.equals("")){
                if (flag==0){
                    sb.append("\""+propname+"\":"+"\""+temp+"\"");
                    flag=1;
                }else {
                    sb.append(",\""+propname+"\":"+"\""+temp+"\"");
                }
            }
        }
        sb.append("}");
        return sb.toString();
    }

}
