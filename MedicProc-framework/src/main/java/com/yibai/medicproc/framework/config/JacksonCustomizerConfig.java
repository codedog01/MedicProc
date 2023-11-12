package com.yibai.medicproc.framework.config;

import cn.hutool.core.date.LocalDateTimeUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

/**
 * <p>
 * jackson全局配置java8 LocalDateTime的序列化 全局配置时间返回格式
 * long转为转为string，解决前端long精度丢失问题，
 * </p>
 *
 * @author 冷澳
 * @date 2023/11/8
 */
@Configuration
public class JacksonCustomizerConfig {

    /**
     * description:适配自定义序列化和反序列化策略，返回前端指定数据类型的数据
     */
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {
        return builder -> {
            builder.serializerByType(LocalDateTime.class, new LocalDateTimeSerializer());
            builder.deserializerByType(LocalDateTime.class, new LocalDateTimeDeserializer());
            builder.serializerByType(Long.class, new LongSerializer());

        };
    }

    /**
     * description:序列化
     * LocalDateTime序列化为毫秒级时间戳
     */
    public static class LocalDateTimeSerializer extends JsonSerializer<LocalDateTime> {
        @Override
        public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers)
                throws IOException {
            if (value != null) {
                String timestamp = String.valueOf(LocalDateTimeUtil.toEpochMilli(value));
                gen.writeString(timestamp);
            }
        }
    }

    /**
     * description:反序列化
     * 毫秒级时间戳序列化为LocalDateTime
     */
    public static class LocalDateTimeDeserializer extends JsonDeserializer<LocalDateTime> {
        @Override
        public LocalDateTime deserialize(JsonParser p, DeserializationContext deserializationContext)
                throws IOException {
            String timestampStr = p.getValueAsString();
            if (StringUtils.isNotEmpty(timestampStr)) {
                return LocalDateTimeUtil.of(Long.parseLong(timestampStr), ZoneOffset.of("+8"));
            } else {
                return null;
            }
        }
    }

    /**
     * description:序列化
     * 将long转为String
     */
    public static class LongSerializer extends JsonSerializer<Long> {
        @Override
        public void serialize(Long l, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
                jsonGenerator.writeString(String.valueOf(l));
        }
    }


}
