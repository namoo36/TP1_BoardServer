package com.namoo.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.util.List;

public class RedisStringMain {
    public static void main(String[] args) {

        try(var jedisPool = new JedisPool("localhost", 6380)) {
            try(Jedis jedis = jedisPool.getResource()) {
                jedis.set("users:300:email", "lee@gmail.com");
                jedis.set("users:300:name", "lee");
                jedis.set("users:300:age", "56");
                String userEmail = jedis.get("users:300:email");
                System.out.println(userEmail);

                List<String> userInfo = jedis.mget("users:300:age", "users:300:email", "users:300:name");
                System.out.println(userInfo.toString());

                long counter = jedis.incr("counter");
                System.out.println(counter);

                long counter1 = jedis.incrBy("counter", 100);
                System.out.println(counter1);

                // 한 번에 요청
                Pipeline pipelined = jedis.pipelined();
                pipelined.set("users:400:email", "park@naver.com");
                pipelined.set("users:400:name", "park");
                pipelined.set("users:400:age", "25");

                List<Object> objects = pipelined.syncAndReturnAll();
                for (Object object : objects) {
                    System.out.println(object.toString());
                }
            }
        }
    }
}
