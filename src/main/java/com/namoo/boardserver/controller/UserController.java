package com.namoo.boardserver.controller;

import com.namoo.boardserver.aop.LoginCheck;
import com.namoo.boardserver.dto.UserDTO;
import com.namoo.boardserver.dto.request.UserDeleteId;
import com.namoo.boardserver.dto.response.UserInfoResponse;
import com.namoo.boardserver.dto.request.UserUpdatePasswordRequest;
import com.namoo.boardserver.dto.request.UserLoginRequest;
import com.namoo.boardserver.dto.response.LoginResponse;
import com.namoo.boardserver.service.impl.UserServiceImpl;
import com.namoo.boardserver.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@Log4j2
@RequiredArgsConstructor    // final 붙은 애 생성자 자동 주입
public class UserController {

    private final UserServiceImpl userService;
    // 실패 시 공통 반환 응답
    private static final ResponseEntity<LoginResponse> FAIL_RESPONSE = new ResponseEntity<LoginResponse>(HttpStatus.BAD_REQUEST);
    private static LoginResponse loginResponse;

    // 회원가입
    @PostMapping("sign-up")
    @ResponseStatus(HttpStatus.CREATED)
    public void signUp(@RequestBody UserDTO userDTO){
        // 입력 받은 값 중복 확인
        if (userDTO.hasNullDataBeforeRegister(userDTO)){
            throw new NullPointerException("회원가입시 필수 데이터를 모두 입력해야 합니다.");
        }
        // 회원 가입
        userService.register(userDTO);
    }

    // 로그인
    @PostMapping("sign-in")
    public HttpStatus login(@RequestBody UserLoginRequest loginRequest,
                            HttpSession session) {

        // ResponseEntity : Spring에서 Http 응답을 나타내는 객체
        // LoginResponse : 돌려줄 객체
        ResponseEntity<LoginResponse> responseEntity = null;

        String userId = loginRequest.getUserId();
        String password = loginRequest.getPassword();

        // 로그인
        UserDTO userInfo = userService.login(userId, password);

        // 로그인 후 회원 Id 확인
        String id = userInfo.getId().toString();

        if (userInfo == null) {
            // 사용자 정보가 조회되지 않을 때
            return HttpStatus.NOT_FOUND;
        } else if (userInfo != null) {
            // 로그인 성공! -> 상태를 success로 바꾸고 사용자 DTO를 담음
            LoginResponse loginResponse = LoginResponse.success(userInfo);

            // 사용자가 관리자일 경우 -> Session에 관리자로 등록
            if (userInfo.getStatus() == (UserDTO.Status.ADMIN))
                SessionUtil.setLoginAdminId(session, id);
            // 일반 사용자인 경우
            else
                SessionUtil.setLoginMemberId(session, id);
            // 반환
            responseEntity = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        } else {
            throw new RuntimeException("Login Error! 유저 정보가 없거나 지워진 유저 정보입니다.");
        }

        return HttpStatus.OK;
    }

    // 내 정보 조회
    @GetMapping("my-info")
    public UserInfoResponse memberInfo(HttpSession session){
        // 세션에서 일반 사용자 id를 반환받음
        String id = SessionUtil.getLoginMemberId(session);

        // 반환되는 값이 없으면 관리자겠구나
        if (id == null) id = SessionUtil.getLoginAdminId(session);

        // 사용자 정보 확인
        UserDTO memberInfo = userService.getUserInfo(id);
        return new UserInfoResponse(memberInfo);
    }

    // 로그아웃
    @PutMapping("log-out")
    public void logout(String accountId, HttpSession session){
        // 세션에서 클리어
        SessionUtil.clear(session);
    }

    // 비밀 번호 업데이트
    @PatchMapping("password-update")
    @LoginCheck(type = LoginCheck.UserType.USER)
    public ResponseEntity<LoginResponse> updateUserPassword(String accountId, @RequestBody UserUpdatePasswordRequest userUpdatePasswordRequest,
                                                            HttpSession session){
        // 응답 객체 생성
        ResponseEntity<LoginResponse> responseEntity = null;
        // 로그인 한 일반 사용자 id 확인
        String id = SessionUtil.getLoginMemberId(session);
        // 이전 비밀번호 추출
        String beforePassword = userUpdatePasswordRequest.getBeforePassword();
        // 새로 바꿀 비밀번호 추출
        String afterPassword = userUpdatePasswordRequest.getAfterPassword();

        try {
            // 비밀번호 업데이트
            userService.updatePassword(id, beforePassword, afterPassword);
            // 객체 반환
            ResponseEntity.ok(new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK));
        } catch (IllegalArgumentException e) {
            log.error("update password 실패", e);
            responseEntity = FAIL_RESPONSE;
        }
        return responseEntity;
    }

    // 계정 삭제
    @DeleteMapping
    public ResponseEntity<LoginResponse> deleteId(@RequestBody UserDeleteId userDeleteId,
                                                  HttpSession session){

        // 응답 객체
        ResponseEntity<LoginResponse> responseEntity = null;
        // 세션에서 일반 사용자 id 추출
        String id = SessionUtil.getLoginMemberId(session);

        try {
            // 로그인 -> userInfo 확인
            UserDTO userInfo = userService.login(id, userDeleteId.getPassword());
            // 계정 삭제
            userService.deleteId(id, userDeleteId.getPassword());
            // 계정 삭제 성공 확인
            LoginResponse loginResponse = LoginResponse.success(userInfo);
            responseEntity = new ResponseEntity<LoginResponse>(loginResponse, HttpStatus.OK);
        }catch (RuntimeException e){
            log.error("deleteId 실패");
            responseEntity = FAIL_RESPONSE;
        }
        return responseEntity;
    }

}
