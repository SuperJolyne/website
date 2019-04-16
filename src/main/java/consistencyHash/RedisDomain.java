package consistencyHash;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class RedisDomain {
    public static HashSet<String> serverNode = new HashSet<String>();

    public static Map<String, JedisPool> jedisPool_Map = new HashMap<String, JedisPool>(16);

    public static ConsistentHash<String> consistentHash = new ConsistentHash<String>(
            new ConsistentHash.HashFunction(), 200, serverNode);

    public static void createPool(String addr_port){
        String[] strings = addr_port.split(":");
        //Redis的地址
        String addr = strings[0];
        //Redis的端口号
        int PORT = Integer.parseInt(strings[1]);
        //可用连接实例的最大数目，默认值为8；
        //如果赋值为-1，则表示不限制；如果pool已经分配了maxActive个jedis实例，则此时pool的状态为exhausted(耗尽)。
        int MAX_ACTIVE = 1024;
        //控制一个pool最多有多少个状态为idle(空闲的)的jedis实例，默认值也是8。
        int MAX_IDLE = 200;
        //等待可用连接的最大时间，单位毫秒，默认值为-1，表示永不超时。如果超过等待时间，则直接抛出JedisConnectionException；
        int MAX_WAIT = 10000;

        //在borrow一个jedis实例时，是否提前进行validate操作；如果为true，则得到的jedis实例均是可用的；
        boolean TEST_ON_BORROW = false;
        JedisPool jedisPool = null;

        try {
            JedisPoolConfig config = new JedisPoolConfig();
            config.setMaxTotal(MAX_ACTIVE);
            config.setMaxIdle(MAX_IDLE);
            config.setMaxWaitMillis(MAX_WAIT);
            config.setTestOnBorrow(TEST_ON_BORROW);
            jedisPool = new JedisPool(config, addr, PORT);
            jedisPool_Map.put(addr_port, jedisPool);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Jedis getJedis(String key) {
        JedisPool jedisPool = jedisPool_Map.get(key);
        try {
            if (jedisPool != null) {
                Jedis resource = jedisPool.getResource();
                return resource;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void returnResource(final Jedis jedis, String key) {
        JedisPool jedisPool = jedisPool_Map.get(key);
        if (jedis != null) {
            jedisPool.returnResourceObject(jedis);
        }
    }
}