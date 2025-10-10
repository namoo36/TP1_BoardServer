package com.namoo.boardserver.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/*
    AWS SNS - SpringBoot 연동
        - AWS 관련 설정값들을 관리하는 설정 객체
        - Spring 시작 시 해당 클래스 로드, 필요한 설정을 빈으로 등록, 주입 가능한 상태가 됨
*/
@Getter
@Configuration
public class AWSConfig {

    // 값을 주입
    @Value("${sns.topic.arn}")
    private String snsTopicARN;

    @Value("${aws.accessKey}")
    private String awsAccessKey;

    @Value("${aws.secretKey}")
    private String awsSecretKey;

    @Value("${aws.region}")
    private String awsRegion;

}
