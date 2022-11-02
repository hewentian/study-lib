package com.hewentian.jackson;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.hewentian.jackson.entity.User;
import com.hewentian.jackson.entity.User2;

import java.io.File;
import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.hewentian.jackson.entity.User3;

/**
 * <p>
 * <b>Test</b> 是
 * </p>
 *
 * @date 2020-09-01 17:49:26
 * @since JDK 1.8
 */
public class Test {
    public static void main(String[] args) throws Exception {
        test1();
        test2();
        test3();
        test4();
        test5();
        test6();
        test7();
        test8();
        test9();
        test10();
        test11();
    }

    static void test1() throws JsonProcessingException {
        String jsonStr = "{\"name\":\"scott\",\"age\":20,\"gender\":\"male\"}";

        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonNode = objectMapper.readTree(jsonStr);
        System.out.println(jsonNode);

        System.out.println(jsonNode.get("name").textValue());
        System.out.println(jsonNode.get("age").intValue());
        System.out.println(jsonNode.get("gender").textValue());
    }

    static void test2() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        ObjectNode objectNode = objectMapper.createObjectNode();
        objectNode.put("name", "scott");
        objectNode.put("age", 20);
        objectNode.put("gender", "male");

        System.out.println(objectNode);
        System.out.println(objectMapper.writeValueAsString(objectNode));
    }

    static void test3() throws JsonProcessingException {
        Map<String, Object> map = new HashMap<>();
        map.put("name", "scott");
        map.put("age", 20);
        map.put("gender", "male");

        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(map));
    }

    static void test4() throws JsonProcessingException {
        User user = new User();
        user.setName("scott");
        user.setAge(20);
        user.setGender("male");
        user.setBirthday(new Date());
        user.setCreateTime(new Date());

        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println(objectMapper.writeValueAsString(user));

        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        System.out.println(objectMapper.writeValueAsString(user));
    }

    static void test5() throws JsonProcessingException {
        String jsonStr = "{\"name\":\"scott\",\"age\":20,\"gender\":\"male\"}";

        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(jsonStr, User.class);

        System.out.println(user);
    }

    static void test6() throws JsonProcessingException {
        User2 user = new User2("scott", 20, "male");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        System.out.println(objectMapper.writeValueAsString(user));
    }

    static void test7() throws JsonProcessingException {
        User3 user = new User3("scott", 20, "male");

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        System.out.println(objectMapper.writeValueAsString(user));
    }

    static void test8() throws JsonProcessingException {
        String jsonStr = "{\"name\":\"scott\",\"age\":20,\"gender\":\"male\",\"school\":\"MIT\"}";

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        User user = objectMapper.readValue(jsonStr, User.class);

        System.out.println(user);
    }

    static void test9() throws JsonProcessingException {
        String jsonStr = "[" +
                "{\"id\":1,\"name\":\"scott\",\"age\":20,\"gender\":\"male\"}, " +
                "{\"id\":2,\"name\":\"tiger\",\"age\":21,\"gender\":\"female\"}, " +
                "{\"id\":3,\"name\":\"ken\",\"age\":23,\"gender\":\"male\"}" +
                "]";

        ObjectMapper objectMapper = new ObjectMapper();

        System.out.println("---------- to array");
        User[] users = objectMapper.readValue(jsonStr, User[].class);
        Arrays.stream(users).forEach(System.out::println);

        System.out.println("---------- to list");
        List<User> userList = objectMapper.readValue(jsonStr, new TypeReference<List<User>>() {
        });
        userList.forEach(System.out::println);
    }

    static void test10() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        String filePath = Test.class.getClassLoader().getResource("user.json").getPath();

        User user = objectMapper.readValue(new File(filePath), User.class);
        System.out.println(user);
    }

    static void test11() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        String filePath = Test.class.getClassLoader().getResource("users.json").getPath();

        User[] users = objectMapper.readValue(new File(filePath), User[].class);
        Arrays.stream(users).forEach(System.out::println);
    }

    static ObjectMapper getObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();

        // 通过该方法对mapper对象进行设置，所有序列化的对象都将按改规则进行序列化
        // Include.ALWAYS 默认
        // Include.NON_DEFAULT 属性为默认值不序列化
        // Include.NON_EMPTY 属性为空（""） 或者为 NULL 都不序列化
        // Include.NON_NULL 属性为 NULL 不序列化
        objectMapper.setSerializationInclusion(JsonInclude.Include.ALWAYS);

        //在未知属性上失败，默认为true
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        // 允许出现特殊字符和转义符，默认false
        objectMapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, false);

        // 允许出现单引号，默认false
        objectMapper.configure(JsonParser.Feature.ALLOW_SINGLE_QUOTES, false);

        // 不用写getter、setter方法
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

        // 友好方式显示日期
        objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

        // 字段保留，将null值转为""
        objectMapper.getSerializerProvider().setNullValueSerializer(new JsonSerializer<Object>() {
            @Override
            public void serialize(Object o, JsonGenerator jsonGenerator,
                                  SerializerProvider serializerProvider)
                    throws IOException {
                jsonGenerator.writeString("");
            }
        });

        return objectMapper;
    }
}
