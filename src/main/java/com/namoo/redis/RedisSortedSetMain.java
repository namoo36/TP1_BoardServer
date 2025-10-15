package com.namoo.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.resps.Tuple;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RedisSortedSetMain {
    public static void main(String[] args) {
        try(var jedisPool = new JedisPool("localhost", 6380)) {
            try (Jedis jedis = jedisPool.getResource()) {

                HashMap<String, Double> scores = new HashMap<>();
                scores.put("user1", 100.0);
                scores.put("user2", 30.0);
                scores.put("user3", 50.0);
                scores.put("user4", 80.0);
                scores.put("user5", 15.0);

                jedis.zadd("game2:scores", scores);

                List<String> zrange = jedis.zrange("game2:scores", 0, Long.MAX_VALUE);
                zrange.forEach(System.out::println);

                List<Tuple> tuples = jedis.zrangeWithScores("game2:scores", 0, Long.MAX_VALUE);
                tuples.forEach(i -> System.out.printf("%s %f \n", i.getElement(), i.getScore() ));

                System.out.println(jedis.zcard("game2:scores"));

                jedis.zincrby("game2:socres", 100.0, "user5");

                List<Tuple> tuples1 = jedis.zrangeWithScores("game2:scores", 0, Long.MAX_VALUE);
                tuples1.forEach(i -> System.out.printf("%s %f \n", i.getElement(), i.getScore() ));
            }
        }
    }
}
