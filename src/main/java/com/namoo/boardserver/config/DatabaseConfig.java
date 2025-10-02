package com.namoo.boardserver.config;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration  // 스프링 설정 클래스임을 명시 -> 이 안에서 정의한 Bean 메서드들이 컨테이너에 등록되서 실행
public class DatabaseConfig {

    @ConfigurationProperties(prefix = "spring.datasource")
    @Bean   // 메서드가 반환하는 객체를 빈으로 등록
    public DataSource dataSource() {
        // datasource 인스턴스 생성 -> 커넥션 풀 역할
        return DataSourceBuilder.create().type(HikariDataSource.class).build();
    }
}