package WriteToIo;

import constants.C_List;
import constants.File_list;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.UUID;

public class DoWrite {

    private static DoWrite write = new DoWrite();

    public static DoWrite getWrite(){
        return write;
    }

    StringBuilder usersb = new StringBuilder();

    StringBuilder wfksb = new StringBuilder();

    private void type(String type,String message){
        if (C_List.USER.equals(type)){
            synchronized (usersb){
                usersb.append(message);
                if (usersb.length()>512){
                    System.out.println("user:"+usersb.toString());
                    doWrite(File_list.User, usersb);
                }
                AutoWrite.bool = true;
            }
        }
        if (C_List.WFK.equals(type)){
            synchronized (wfksb){
                wfksb.append(message);
                if (wfksb.length()>512){
                    System.out.println("wfk:"+wfksb.toString());
                    doWrite(File_list.Wfk, wfksb);
                }
                AutoWrite.bool = true;
            }
        }
    }

    public void doWrite(String file,StringBuilder sb){
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            raf.seek(raf.length());
            String w = sb.toString();
            raf.write(w.getBytes("utf-8"));
            raf.writeBytes("\r");
            raf.close();
            sb.delete(0, sb.length());
            AutoWrite.bool = false;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        int j = 0;
        for (int i=0; i<10000; i++) {
            if (i<5000){
                Thread thread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        DoWrite t = getWrite();
                        UUID id = UUID.randomUUID();
                        String s = id.toString().replaceAll("-", "");
                        t.type(C_List.USER, s);
                    }
                }) {};
                thread.start();
            }
            Thread thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    DoWrite t = getWrite();
                    UUID id = UUID.randomUUID();
                    String s = id.toString().replaceAll("-", "");
                    t.type(C_List.WFK, s);
                }
            }) {};
            thread.start();

        }
    }
}
