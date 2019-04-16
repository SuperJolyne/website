package WriteToIo;

import constants.File_list;

import java.util.concurrent.TimeUnit;

public class AutoWrite {
    public static volatile boolean bool = false;

    private void doAutoWrite(){
        while (true){
            if (bool){ //如果有日志就超过30分钟没有落盘，则强制落盘
                DoWrite write = DoWrite.getWrite();
                if (write.usersb.length()>0){
                    write.doWrite(File_list.User, write.usersb);
                }
                if (write.wfksb.length()>0){
                    write.doWrite(File_list.Wfk, write.wfksb);
                }
            }else {
                try { //如果已有日志已经落盘了，则继续睡眠
                    TimeUnit.HOURS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }


}
