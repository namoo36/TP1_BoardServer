package com.namoo.boardserver.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

// MyBatis 설정 클래스

@Configuration  // 스프링 설정 클래스라는 의미
@MapperScan(basePackages = "com.namoo.boardserver.mapper")  // 해당 패키지 안의 매퍼 인터페이스를 자동 스캔, 빈 등록
public class MySqlConfig {

    // MyBatis에서 MySQl 세션을 만드는 핵심 객체
    // 스프링이 관리하는 커넥션 풀 DataSource를 주입 받음
    @Bean
    public SqlSessionFactory sqlSessionFactory(DataSource dataSource) throws Exception {

        // MyBatis가 DB에 연결할 때 사용할 커넥션 풀 지정
        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(dataSource);

        // 매퍼 XML설정
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sessionFactory.setMapperLocations(resolver.getResources("classpath:mappers/*.xml"));

        // MyBatis XML 전역 설정 파일 설정
        Resource myBatisConfig = new PathMatchingResourcePatternResolver().getResource("classpath:mybatis-config.xml");
        sessionFactory.setConfigLocation(myBatisConfig);

        // 최종적으로 SqlSessionFactory 객체 반환
        return sessionFactory.getObject();
    }
}
