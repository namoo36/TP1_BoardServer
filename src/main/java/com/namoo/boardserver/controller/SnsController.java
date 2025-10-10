package com.namoo.boardserver.controller;

import com.amazonaws.services.ec2.model.SubnetState;
import com.namoo.boardserver.config.AWSConfig;
import com.namoo.boardserver.service.SnsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.*;

import java.util.Map;

@RestController
@Log4j2
@RequiredArgsConstructor    // 생성자 자동 주입
public class SnsController {
    // REST API 컨트롤러

    private final AWSConfig awsConfig;
    private final SnsService snsService;

    // 주제 생성
    @PostMapping("/create-topic")
    public ResponseEntity<String> createTopic(@RequestParam final String topicName) {
        // 토픽 이름을 RequestParam으로 전달 받음

        // 전달 받은 요청을 바탕으로 AWS SDK의 SNS 토픽 생성 요청 객체를 생성
        final CreateTopicRequest createTopicRequest = CreateTopicRequest.builder()
                .name(topicName)
                .build();

        // SNS에 연결하는 객체 생성
        SnsClient snsClient = snsService.getSnsClient();
        // SNS에 토픽 생성을 명령
        final CreateTopicResponse createTopicResponse = snsClient.createTopic(createTopicRequest);

        // 생성이 제대로 안되었다면 -> 예외 던짐
        if(!createTopicResponse.sdkHttpResponse().isSuccessful()) {
            throw getResponseStatusException(createTopicResponse);
        }

        // 제대로 토픽이 생성되었다면 -> AWS SNS에 topicName으로 된 주제가 생성됨
        log.info("Created topic {}", createTopicResponse.topicArn());
        snsClient.close();
        return new ResponseEntity<>("TOPIC CREATING SUCCESS", HttpStatus.OK);
    }

    // 예외 변환기
    // AWS SDK에서 에러가 발생했을 때 ResponseStatusException로 반환
    private ResponseStatusException getResponseStatusException(SnsResponse snsResponse) {
        return new ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR, snsResponse.sdkHttpResponse().statusText().get()
        );
    }

    // 구독자 등록
    // endPoint : 알림을 받고자 하는 내 서버, SNS가 알림을 보내는 대상 서버
    // topicArn : 구독하려는 SNS Topic ARN
    @PostMapping("/subscribe")
    public ResponseEntity<String> subscribe(@RequestParam final String endPoint,
                                            @RequestParam final String topicArn) {

        // 구독 요청을 위한 객체 생성
        final SubscribeRequest subscribeRequest = SubscribeRequest.builder()
                .protocol("https")  // SNS가 알림을 전달할 방식을 지정
                .topicArn(topicArn)     // 구독할 SNS 주제
                .endpoint(endPoint)     // 실제 구독자 URL
                .build();

        // SNS에 연결하는 클라이언트 생성
        SnsClient snsClient = snsService.getSnsClient();
        // SNS에 구독 요청을 전달
        final SubscribeResponse subscribeResponse = snsClient.subscribe(subscribeRequest);

        // 요청 성공 여부 확인
        if(!subscribeResponse.sdkHttpResponse().isSuccessful()) {
            throw getResponseStatusException(subscribeResponse);
        }
        log.info("topic ARN to subscribe = " + subscribeResponse.subscriptionArn());
        return new ResponseEntity<>("TOPIC SUBSCRIBING SUCCESS", HttpStatus.OK);
    }

    // SNS Topic에 메시지를 발행 -> 발행 시 해당 Topic을 구독 중이던 주체들에게 알림이 감
    @PostMapping("/publish")
    public String publish(@RequestParam String topicArn,    // 메시지를 보낼 Topic ARN 주소
                          @RequestBody Map<String, Object> message  // 클라이언트가 전송하는 메시지 본문
    ){
        // SNS 클라이언트 생성
        SnsClient snsClient = snsService.getSnsClient();
        // SNS에 메시지 발행 요청을 담는 객체 생성
        final PublishRequest publishRequest = PublishRequest.builder()
                .topicArn(topicArn)     // 해당 SNS TOPIC으로
                .subject("HTTP ENDPOINT TESt MESSAGE")  // 이 메시지 제목으로
                .message(message.toString())    // 이 메시지를 보내라
                .build();

        // 메시지 발행 후 응답을 받음
        PublishResponse publishResponse = snsClient.publish(publishRequest);
        log.info("message : " + publishResponse.sdkHttpResponse().statusCode());
        snsClient.close();

        return "sent MSG ID = " + publishResponse.messageId();
    }


}
