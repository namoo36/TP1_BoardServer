package com.namoo.boardserver.exception.handler;

import com.namoo.boardserver.dto.response.CommonResponse;
import com.namoo.boardserver.exception.BoardServerException;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler( {RuntimeException.class})
    public ResponseEntity<Object> handleRunTimeException(RuntimeException e){
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "RuntimeException", e.getMessage(), e.getCause());
        return new ResponseEntity<>(commonResponse, commonResponse.getStatus());
    }

    @ExceptionHandler( {BoardServerException.class})
    public ResponseEntity<Object> handleBoardServerException(BoardServerException e){
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "BoardServerException", e.getMessage(), e.getCause());
        return new ResponseEntity<>(commonResponse, commonResponse.getStatus());
    }

    @ExceptionHandler( {Exception.class})
    public ResponseEntity<Object> handleException(Exception e){
        CommonResponse commonResponse = new CommonResponse(HttpStatus.OK, "Exception", e.getMessage(), e.getCause());
        return new ResponseEntity<>(commonResponse, commonResponse.getStatus());
    }
}
