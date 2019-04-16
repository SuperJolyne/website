package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class WriteUtils {

    public static String user = "/home/super/Business/log/user.log";
    public static String wfk = "/home/super/Business/log/wfk.log";
    public static String fh = "/home/super/Business/log/fh.log";
    public static String qx = "/home/super/Business/log/qx.log";
    public static String tk = "/home/super/Business/log/tk.log";

    public static String userlog = "";
    public static String wfklog = "";
    public static String fhlog = "";
    public static String qxlog = "";
    public static String tklog = "";

    public static int usert = 0;
    public static int wfkt = 0;
    public static int fht = 0;
    public static int qxt = 0;
    public static int tkt = 0;


    public static void doWrite(String add){
        try {
            RandomAccessFile raf = new RandomAccessFile(add, "rw");
            long fileLength = raf.length();
            raf.seek(fileLength);
            raf.write(userlog.getBytes("utf-8"));
            raf.close();
            userlog = "";
            usert = 0;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }



    }

    public static void doBuffer(String log, String data) throws ClassNotFoundException {
        if ("userlog".equals(log)){
            userlog = userlog+data+"\n";
            System.out.println(userlog.getBytes().length);
            usert++;
            if (usert>=5){
                doWrite(user);
            }
        }
        if ("wfklog".equals(log)){
            wfklog = wfklog+data+"\n";
            System.out.println(wfklog.getBytes().length);
            wfkt++;
            if (wfkt>=5){
                doWrite(wfk);
            }
        }
        if ("fhlog".equals(log)){
            fhlog = fhlog+data+"\n";
            System.out.println(fhlog.getBytes().length);
            fht++;
            if (fht>=5){
                doWrite(fh);
            }
        }
        if ("qxlog".equals(log)){
            qxlog = qxlog+data+"\n";
            System.out.println(qxlog.getBytes().length);
            qxt++;
            if (qxt>=5){
                doWrite(qx);
            }
        }
        if ("tklog".equals(log)){
            tklog = tklog+data+"\n";
            System.out.println(tklog.getBytes().length);
            tkt++;
            if (tkt>=5){
                doWrite(tk);
            }
        }
    }


    public static void main(String[] args) throws ClassNotFoundException {
        long t1 = System.currentTimeMillis();
        String a = "userlog";
        doBuffer(a, "user:00000031,aaaaaa,男,176002200404,741774821@qq.com,7890cou:00000001,2019-01-25    1548409693964");
        doBuffer(a, "user:00000031,aaaaaaa,男,176002200404,741774821@qq.com,7890cou:00000001,2019-01-25    1548409693964");
        doBuffer(a, "user:00000031,aaaaaa,男,176002200404,741774821@qq.com,7890cou:00000001,2019-01-25    1548409693964");
        doBuffer(a, "user:00000031,aaaaaa,男,176002200404,741774821@qq.com,7890cou:00000001,2019-01-25    1548409693964");
        doBuffer(a, "user:00000031,aaaaaa,男,176002200404,741774821@qq.com,7890cou:00000001,2019-01-25    1548409693964");
        doBuffer(a, "user:00000031,aaaaa,男,176002200404,741774821@qq.com,7890cou:00000001,2019-01-25    1548409693964");
        long t2 = System.currentTimeMillis();
        System.out.println("time:"+(t2-t1));
    }
}
