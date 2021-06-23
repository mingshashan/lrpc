package com.mss.gtr.lrpc.protocol.serialization;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mss.gtr.lrpc.core.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * json 序列化、反序列化对象，使用jackson
 */
public class JsonSerialization implements LRpcSerialization {

    private static final Logger logger = LoggerFactory.getLogger(JsonSerialization.class);

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = getObjectMapper();
    }

    private static ObjectMapper getObjectMapper() {
        ObjectMapper customMapper = new ObjectMapper();

        customMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);
        customMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, true);
        customMapper.configure(DeserializationFeature.FAIL_ON_NUMBERS_FOR_ENUMS, true);
        customMapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"));

        return customMapper;
    }

    @Override
    public <T> byte[] serialize(T data) throws IOException {
        Assert.notNull(data, "the serialize data must not be null");

        byte[] results = null;

        long beginTime = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            logger.debug("jackson start serialize data...");
        }
        try {
            results = OBJECT_MAPPER.writeValueAsBytes(data);
            if (logger.isDebugEnabled()) {
                logger.debug("jackson end serialize data, cost time = {}", (System.currentTimeMillis() - beginTime));
            }
        } catch (Exception e) {
            logger.error("jackson serialize date error, cost time = {}", (System.currentTimeMillis() - beginTime));
            throw new SerializationException(e);
        }

        return results;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> type) throws IOException {
        Assert.notNull(data, "the deserialize data must not be null");
        Assert.notNull(type, "the deserialize data class type must not be null");

        T result = null;
        long beginTime = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            logger.debug("jackson begin deserialize data, class type = {}", type.getCanonicalName());
        }
        try {
            result = OBJECT_MAPPER.readValue(data, type);

            if (logger.isDebugEnabled()) {
                logger.debug("jackson end deserialize data, class type = {}, cost time = {}", type.getCanonicalName(), (System.currentTimeMillis() - beginTime));
            }
        } catch (Exception e) {
            logger.error("jackson deserialize data error, cost time = {}", (System.currentTimeMillis() - beginTime));
            throw new SerializationException(e);
        }

        return result;
    }
}
