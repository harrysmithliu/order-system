package com.harry.order.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
public class JacksonConfig {

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();

        // 注册JavaTimeModule来处理Java 8日期时间
        JavaTimeModule javaTimeModule = new JavaTimeModule();

        // 配置LocalDateTime序列化/反序列化
        DateTimeFormatter localDateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        javaTimeModule.addSerializer(LocalDateTime.class,
                new LocalDateTimeSerializer(localDateTimeFormatter));
        javaTimeModule.addDeserializer(LocalDateTime.class,
                new LocalDateTimeDeserializer(localDateTimeFormatter));

        // 配置OffsetDateTime - 使用ISO格式（OffsetDateTime天生支持）
        javaTimeModule.addSerializer(OffsetDateTime.class, new JsonSerializer<OffsetDateTime>() {
            @Override
            public void serialize(OffsetDateTime value, JsonGenerator gen, SerializerProvider provider)
                    throws IOException {
                gen.writeString(value.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            }
        });

        javaTimeModule.addDeserializer(OffsetDateTime.class, new JsonDeserializer<OffsetDateTime>() {
            @Override
            public OffsetDateTime deserialize(JsonParser p, DeserializationContext ctxt)
                    throws IOException {
                String value = p.getValueAsString();
                try {
                    // 尝试自定义格式
                    return OffsetDateTime.parse(value + "+00:00",
                            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ssZ"));
                } catch (Exception e) {
                    // 降级到ISO格式
                    return OffsetDateTime.parse(value);
                }
            }
        });

        mapper.registerModule(javaTimeModule);

        // 禁用时间戳格式，使用格式化字符串
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        mapper.disable(DeserializationFeature.ADJUST_DATES_TO_CONTEXT_TIME_ZONE);
        mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);

        return mapper;
    }
}
