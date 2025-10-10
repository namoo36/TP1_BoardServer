package com.namoo.boardserver.service;

import com.namoo.boardserver.config.AWSConfig;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

@Service
@AllArgsConstructor
public class SnsService {
    // AWS SNS 메시지를 발행하기 위한 클라이언트 생성, 정보 주입 역할

    // 설정주입
    AWSConfig awsConfig;

    // AWS 자격 증명 만드는 역할
    public AwsCredentialsProvider getAWSCredential(String accessKey, String secretKey) {
        // AWS SDK를사용할 떄 필요한 인증 정보 객체를 생성하는 메서드
        AwsBasicCredentials awsCreds = AwsBasicCredentials.create(accessKey, secretKey);
        return () -> awsCreds;
        // 이 사용자는 이 공개키, 비밀키를 가지고 있는 사용자입니다 -> 를 AWS SDK에게 알려줌
    }

    // 실제 AWS SNS에 연결하는 클라이언트 객체를 만드는 메서드
    public SnsClient getSnsClient() {
        return SnsClient.builder()
                .credentialsProvider(
                        getAWSCredential(awsConfig.getAwsAccessKey(), awsConfig.getAwsSecretKey())
                ).region(Region.of(awsConfig.getAwsRegion()))
                .build();
    }
}
