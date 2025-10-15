package com.namoo.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Set;

public class RedisListSetMain {
    public static void main(String[] args) {

        try(var jedisPool = new JedisPool("localhost", 6380)) {
            try (Jedis jedis = jedisPool.getResource()) {
                // list
                // 1.stack
                jedis.rpush("stack1", "aaaa");
                jedis.rpush("stack1", "bbbb");
                jedis.rpush("stack1", "cccc");

                List<String> stack1 = jedis.lrange("stack1", 0, -1);
                for (String s : stack1) {
                    System.out.println(s);
                }

                System.out.println(jedis.rpop("stack1"));
                System.out.println(jedis.rpop("stack1"));
                System.out.println(jedis.rpop("stack1"));

                // 2. queue
                jedis.rpush("queue1", "zzzz");
                jedis.rpush("queue1", "aaaa");
                jedis.rpush("queue1", "cccc");

                System.out.println(jedis.lpop("stack1"));
                System.out.println(jedis.lpop("stack1"));
                System.out.println(jedis.lpop("stack1"));

                // 3, block
                List<String> blpop = jedis.blpop(10, "queue:block");
                if(blpop != null){
                    blpop.forEach(System.out::println);
                }


                // set

                jedis.sadd("users:500:follow", "100", "200", "300", "400");
                jedis.srem("users:500:follow", "100");

                Set<String> smembers = jedis.smembers("users:500:follow");
                smembers.forEach(System.out::println);

                boolean sismember = jedis.sismember("users:500:follow", "100");
                System.out.println(sismember);
                boolean sismember1 = jedis.sismember("users:500:follow", "200");
                System.out.println(sismember1);

                long scard = jedis.scard("users:500:follow");
                System.out.println(scard);

                jedis.sadd("users:501:follow", "100", "200", "500", "600");
                Set<String> sinter = jedis.sinter("users:500:follow", "users:501:follow");
                sinter.forEach(System.out::println);
            }
        }
    }
}
