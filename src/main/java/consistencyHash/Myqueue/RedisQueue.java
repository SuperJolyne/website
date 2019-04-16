package consistencyHash.Myqueue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class RedisQueue {
    public static BlockingQueue<String> queue = new ArrayBlockingQueue(1000);

}
