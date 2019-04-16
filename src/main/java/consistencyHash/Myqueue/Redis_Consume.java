package consistencyHash.Myqueue;

import consistencyHash.RedisDomain;
import consistencyHash.redisThreads.Move;

import java.util.concurrent.BlockingQueue;

public class Redis_Consume extends Thread {
    private BlockingQueue<String> queue ;
    public Redis_Consume(BlockingQueue<String> queue){
        this.queue = queue;
    }
    public void run(){
        while (true){
            String data = null;
            try {
                data = queue.take();
                RedisDomain.consistentHash.addNode(data); //往hash环添加节点//此为虚拟节点
                RedisDomain.serverNode.add(data);//记录实体节点
                RedisDomain.createPool(data);//将对应的端口创建对应的jedispool
                Move move = new Move();
                move.doMove(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
