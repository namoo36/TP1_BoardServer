package com.namoo.boardserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@EnableCaching	// redis 캐시 설정
@SpringBootApplication
public class BoardserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(BoardserverApplication.class, args);
	}

}
