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

    public String buscarDado(String chave) {
        return (String) redisTemplate.opsForValue().get(chave);
    }

    @CachePut(value = "dados", key = "#chave")
    public String salvarDadoComCache(String chave, String valor) {
        return valor;
    }

    @Cacheable(value = "dados", key = "#chave")
    public String buscarDadoComCache(String chave) {
        return "Buscando do banco...";
    }

    @CacheEvict(value = "dados", key = "#chave")
    public void removerDado(String chave) {
        redisTemplate.delete(chave);
    }

}
