package com.samsamhajo.deepground.external.redis;

public class RedisKey {

    private final String key;

    private RedisKey(String key) {
        this.key = key;
    }

    public static RedisKey of(String key, Object... args) {
        return new RedisKey(String.format(key, args));
    }

    @Override
    public String toString() {
        return key;
    }
}
