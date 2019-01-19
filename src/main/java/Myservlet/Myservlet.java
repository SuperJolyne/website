package Myservlet;

import contrual.*;
import pojo.Data;
import pojo.Status;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Myservlet {
    public static Status doServlet(Data data, String url) throws ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Status status = new Status();
        String[] urls = url.split("[/]");
        System.out.println(urls[1]);
        Class cls = null;
        Constructor constructor = null;
        switch (urls[1]){
            case "Register":
                cls = Class.forName("contrual."+urls[1]);
                constructor = cls.getConstructor();
                Register register = (Register) constructor.newInstance();
                Method doRegisters = cls.getMethod(urls[2],Data.class);
                status = (Status)doRegisters.invoke(register,data);
                break;

            case "Cart":
                cls = Class.forName("contrual."+urls[1]);
                constructor = cls.getConstructor();
                Cart cart = (Cart) constructor.newInstance();
                Method doCart = cls.getMethod(urls[2],Data.class);
                status = (Status)doCart.invoke(cart,data);
                break;

            case "User":
                cls = Class.forName("contrual."+urls[1]);
                constructor = cls.getConstructor();
                User user = (User) constructor.newInstance();
                Method doUser = cls.getMethod(urls[2],Data.class);
                status = (Status)doUser.invoke(user,data);
                break;

            case "Order":
                cls = Class.forName("contrual."+urls[1]);
                constructor = cls.getConstructor();
                Order order = (Order) constructor.newInstance();
                Method doOrder = cls.getMethod(urls[2],Data.class);
                status = (Status)doOrder.invoke(order,data);
                break;

            case "Goods":
                cls = Class.forName("contrual."+urls[1]);
                constructor = cls.getConstructor();
                Goods goods = (Goods) constructor.newInstance();
                Method doGoods = cls.getMethod(urls[2],Data.class);
                status = (Status)doGoods.invoke(goods,data);
                break;

            case "Collection":
                cls = Class.forName("contrual."+urls[1]);
                constructor = cls.getConstructor();
                Collection collection1 = (Collection) constructor.newInstance();
                Method delete1 = cls.getMethod(urls[2],Data.class);
                status = (Status)delete1.invoke(collection1,data);
                break;
        }
        return status;
    }
}
