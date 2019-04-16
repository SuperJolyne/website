package consistencyHash.redisThreads;

import consistencyHash.RedisDomain;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Tuple;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Move_thread extends Thread {

    String ori_node;//初始的节点

    public Move_thread(String ori_node){
        this.ori_node = ori_node;
    }

    public void run(){
        //初始节点的jedis
        Jedis jedis = RedisDomain.getJedis(ori_node);

        //获取所有的key
        Set<String> key_Set = jedis.keys("*");

        try {
            //获取所有的key后先睡眠10s，避免进入死循环
            Thread.sleep(10000);
        //将所有的key进行迁移
        for (String key : key_Set){
            String type = jedis.type(key);
            //重新将原本的key进行hash分配到对应的节点上
            if ("string".equals(type)){
                String value = jedis.get(key);
                String node = RedisDomain.consistentHash.getNode(key);//重新获取节点
                if (node == ori_node){
                    continue;
                }
                Jedis jedis_node = RedisDomain.getJedis(node);
                jedis_node.set(key, value);
                jedis.del(key);
                RedisDomain.returnResource(jedis_node, node);
                RedisDomain.returnResource(jedis, ori_node);
            }
            if ("hash".equals(type)){
                Map<String, String> value = jedis.hgetAll(key);
                String node = RedisDomain.consistentHash.getNode(key);
                if (node == ori_node){
                    continue;
                }
                Jedis jedis_node = RedisDomain.getJedis(node);
                jedis_node.hmset(key, value);
                jedis.del(key);
                RedisDomain.returnResource(jedis_node, node);
                RedisDomain.returnResource(jedis, ori_node);
            }
            if ("set".equals(type)){
                Set<String> value_set = jedis.smembers(key);
                String node = RedisDomain.consistentHash.getNode(key);
                if (node == ori_node){
                    continue;
                }
                Jedis jedis_node = RedisDomain.getJedis(node);
                for (String v : value_set){
                    jedis_node.sadd(key , v);
                }
                jedis.del(key);
                RedisDomain.returnResource(jedis_node, node);
                RedisDomain.returnResource(jedis, ori_node);
            }
            if ("zset".equals(type)){
                Set<Tuple> value_set = jedis.zrangeWithScores(key, 0, -1);
                String node = RedisDomain.consistentHash.getNode(key);
                if (node == ori_node){
                    continue;
                }
                Jedis jedis_node = RedisDomain.getJedis(node);
                for (Tuple v : value_set){
                    jedis_node.zadd(key, v.getScore(), v.getElement());
                }
                jedis.del(key);
                RedisDomain.returnResource(jedis_node, node);
                RedisDomain.returnResource(jedis, ori_node);
            }
            if ("list".equals(type)){
                List<String> value = jedis.lrange(key, 0, -1);
                String node = RedisDomain.consistentHash.getNode(key);
                if (node == ori_node){
                    continue;
                }
                Jedis jedis_node = RedisDomain.getJedis(node);
                for (String v : value){
                    jedis_node.lpush(key, v);
                }
                jedis.del(key);
                RedisDomain.returnResource(jedis_node, node);
                RedisDomain.returnResource(jedis, ori_node);
            }
        }

        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("节点"+ori_node+"移动数据失败");
        }

    }
}
