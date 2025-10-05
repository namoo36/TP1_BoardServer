package com.namoo.boardserver.config;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;


/*
    SpringBoot에서 Redis를 Cache용으로 설정하고 JSON 직렬화 설정하는 구성 클래스
    - 스프링에서는 캐시에 객체를 저장할 때 객체를 직렬화해야 함
    - ObjectMapper : Java 객체 → JSON 문자열로 직렬화
* */
@Configuration
public class RedisConfig {

    // application.properties에 정의된 값을 주입
    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.password}")
    private String redisPwd;

    @Value("${expire.defaultTime}")
    private Long defaultTime;

    // JSON 직렬화/역직렬화를 담당하는 ObjectMapper를 빈으로 등록
    @Bean
    public ObjectMapper objectMapper(){
        // Java 객체 ↔ JSON 변환 담당
        ObjectMapper mapper = new ObjectMapper();

        // 날짜를 타임스탬프가 아닌 ISO-8601 문자열로 직렬화
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // java8 날짜 타입을 JSON으로 안전하게 직렬화/역직렬화함
        mapper.registerModule(new JavaTimeModule());
        return mapper;
    }

    // Redis 서버에 연결하기 위한 팩토리를 만드는 Bean
    // Factory : 필요할 때 객체를 만들어주는 공장
    @Bean   // 컨테이너가 관리하는 싱글톤 객체 -> Bean이 있으면 있는거 쓰고, 없으면 Factory 로직으로 새 객체를 만듬
    public RedisConnectionFactory redisConnectionFactory(){

        // RedisStandaloneConfiguration : Redis가 단일 서버일 때 사용하는 설정
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        // Redis 호스트명, 포트 번호, 비밀번호 설정
        redisStandaloneConfiguration.setHostName(redisHost);
        redisStandaloneConfiguration.setPort(redisPort);
        redisStandaloneConfiguration.setPassword(redisPwd);

        // LettuceConnectionFactory : Redis와 실제 통신을 담당하는 클라이언트 라이브러리
        LettuceConnectionFactory connectionFactory = new LettuceConnectionFactory(redisStandaloneConfiguration);
        return connectionFactory;
    }

    // Redis 연결을 사용해서 캐시에 접근/조작하는 매니저
    //      - 캐시에 데이터를 저장하거나 가져올 때 사용하는 "관리자 객체"
    //      - 내부에서 RedisConnectionFactory를 이용해 Redis 서버와 연결
    //      - 캐시 만료 시간, 직렬화 방식 등 캐시 정책(configuration) 적용
    @Bean
    public RedisCacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory,
                                               ObjectMapper objectMapper){
        // 기본 Redis 캐시 설정 객체를 가져옴
        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig()
                .disableCachingNullValues() // null 값은 캐시에 저장하지 않음
                .entryTtl(Duration.ofSeconds(defaultTime)) // 만료 시간 : defaultTime 값 만큼 지나면 자동 삭제
                .serializeValuesWith(RedisSerializationContext  // 캐시 저장 값을 어떻게 직렬화할 건지 지정
                        .SerializationPair
                        .fromSerializer(new StringRedisSerializer()))   // 문자열 형태로 직렬화
                .serializeValuesWith(RedisSerializationContext
                        .SerializationPair
                        .fromSerializer(new GenericJackson2JsonRedisSerializer(objectMapper))); // JSON 형태로 직렬화 -> Redis 저장

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory)  // Redis 서버 연결 정보 넣기
                .cacheDefaults(configuration)   // 위에서 만든 캐시 설정(configuration) 적용
                .build();
    }
}
