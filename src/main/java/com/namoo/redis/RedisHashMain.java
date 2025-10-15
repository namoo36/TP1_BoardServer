package com.namoo.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedisHashMain {
    public static void main(String[] args) {
        try(var jedisPool = new JedisPool("localhost", 6380)) {
            try (Jedis jedis = jedisPool.getResource()) {
                // hash

                jedis.hset("users:2:info", "name", "greg");

                HashMap<String, String> map = new HashMap<>();
                map.put("name", "greg2");
                map.put("phone", "010-xxxx-yyyy");
                jedis.hset("users:2:info", map);

                jedis.hdel("users:2:info", "phone");

                System.out.println(jedis.hget("users:2:info", "name"));
                Map<String, String> maps = jedis.hgetAll("users:2:info");
                maps.forEach((k, v) -> {
                    System.out.println("key : " + k + ", value : " + v);
                });

                // hincr
                jedis.hincrBy("users:2:info", "visited", 0);
                jedis.hincrBy("users:2:info", "visited", 1);
                jedis.hincrBy("users:2:info", "visited", 30);

            }
        }
    }
}
