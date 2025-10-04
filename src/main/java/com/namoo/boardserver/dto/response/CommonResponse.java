package com.namoo.boardserver.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

// 제너릭 문법 사용
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse<T> {
    private HttpStatus status;
    private String code;
    private String message;
    private T data; // 본문
}
