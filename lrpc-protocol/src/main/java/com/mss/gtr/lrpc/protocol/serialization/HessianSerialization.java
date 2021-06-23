package com.mss.gtr.lrpc.protocol.serialization;

import com.caucho.hessian.io.HessianSerializerInput;
import com.caucho.hessian.io.HessianSerializerOutput;
import com.mss.gtr.lrpc.core.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * hessian序列化、反序列化对象
 */
public class HessianSerialization implements LRpcSerialization {

    private static Logger logger = LoggerFactory.getLogger(HessianSerialization.class);

    @Override
    public <T> byte[] serialize(T data) throws IOException {
        Assert.notNull(data, "the serialize data must not be null");
        byte[] results = null;

        long beginTime = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            logger.debug("hessian start serialize data...");
        }
        HessianSerializerOutput hessianSerializerOutput = null;
        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            hessianSerializerOutput = new HessianSerializerOutput(byteArrayOutputStream);
            hessianSerializerOutput.startMessage();
            hessianSerializerOutput.writeObject(data);
            hessianSerializerOutput.flush();
            hessianSerializerOutput.completeMessage();
            results = byteArrayOutputStream.toByteArray();
            if (logger.isDebugEnabled()) {
                logger.debug("hessian end serialize data, cost time = {}", (System.currentTimeMillis() - beginTime));
            }
        } catch (Exception e) {
            logger.error("hessian serialize date error, cost time = {}", (System.currentTimeMillis() - beginTime));
            throw new SerializationException(e);
        } finally {
            if (null != hessianSerializerOutput) {
                hessianSerializerOutput.close();
            }
        }

        return results;
    }

    @Override
    public <T> T deserialize(byte[] data, Class<T> type) throws IOException {
        Assert.notNull(data, "the deserialize data must not be null");
        Assert.notNull(type, "the deserialize data class type must not be null");

        T result = null;
        HessianSerializerInput serializerInput = null;
        long beginTime = System.currentTimeMillis();
        if (logger.isDebugEnabled()) {
            logger.debug("hessian begin deserialize data, class type = {}", type.getCanonicalName());
        }
        try (ByteArrayInputStream inputStream = new ByteArrayInputStream(data)) {
            serializerInput = new HessianSerializerInput(inputStream);
            serializerInput.addRef(type);
            serializerInput.startMessage();
            result = (T) serializerInput.readObject(type);
            if (logger.isDebugEnabled()) {
                logger.debug("hessian end deserialize data, class type = {}, cost time = {}", type.getCanonicalName(), (System.currentTimeMillis() - beginTime));
            }
        } catch (Exception e) {
            logger.error("hessian deserialize data error, cost time = {}", (System.currentTimeMillis() - beginTime));
            throw new SerializationException(e);
        } finally {
            if (null != serializerInput) {
                serializerInput.close();
            }
        }

        return result;
    }
}
