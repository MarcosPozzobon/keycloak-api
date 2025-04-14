package com.marcos.desenvolvimento.authorization_ms.service;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    public void setAuthenticationCache(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    /*
    public List<String> getRoles(String key) {
        return redisTemplate.opsForValue().get(key); // corrigir
        //return (String) redisTemplate.opsForValue().get(chave);
    }*/

    @Cacheable(value = "data", key = "#key")
    public String findCachedTokenAndPermissions(String token) {
        return (String) redisTemplate.opsForValue().get(token);
    }

    @CacheEvict(value = "dados", key = "#chave")
    public void removerDado(String chave) {
        redisTemplate.delete(chave);
    }

}
