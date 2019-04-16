package consistencyHash.Myqueue;

import java.util.concurrent.BlockingQueue;

public class Redis_Product extends Thread{
    private BlockingQueue<String> queue ;
    public Redis_Product(BlockingQueue<String> queue){
        this.queue = queue;
    }
    public void run(String s){
        try {
            queue.put(s);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
