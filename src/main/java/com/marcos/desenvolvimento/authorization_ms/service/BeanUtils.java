package com.marcos.desenvolvimento.authorization_ms.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class BeanUtils {

    private final ObjectMapper objectMapper;

    public String toJson(Object object){
        try{
            return objectMapper.writeValueAsString(object);
        }catch (Exception e){
            log.info("Failed to convert object to JSON: " + e.getCause());
        }
        return ""; // failed to serialize
    }

}
