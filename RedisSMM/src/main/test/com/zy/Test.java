package com.zy;

import redis.clients.jedis.Jedis;

import java.util.Set;

public class Test {
    public static void main(String[] args) {
        Jedis jedis = new Jedis("192.168.23.128", 6379);
        Set<String> keys = jedis.keys("*");
        for (String key : keys) {
            System.out.println(key);
        }

   //  jedis.set("age","18");


    jedis.close();
    }
}
