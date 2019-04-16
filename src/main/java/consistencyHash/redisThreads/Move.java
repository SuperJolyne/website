package consistencyHash.redisThreads;

import consistencyHash.RedisDomain;

import java.util.HashSet;

public class Move {
    public void doMove(String node){
        //将实际节点克隆出来，用于移动节点的数据
        HashSet<String> serverNode = (HashSet<String>) RedisDomain.serverNode.clone();
        serverNode.remove(node);//移除新添加进来的节点
        int size = serverNode.size();
        if (size<1){
            return;
        }
        for (String s : serverNode){
            Move_thread mt = new Move_thread(s); //用之前的节点新建对应的线程进行重新划分
            mt.start();
        }
    }
}
