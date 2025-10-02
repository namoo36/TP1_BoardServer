package com.namoo.boardserver.service.impl;

import com.namoo.boardserver.dto.UserDTO;
import com.namoo.boardserver.exception.DuplicateIdException;
import com.namoo.boardserver.mapper.UserProfileMapper;
import com.namoo.boardserver.service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

import static com.namoo.boardserver.utils.SHA256Util.encryptionSHA256;

@Service
@Log4j2
public class UserServiceImpl implements UserService {

    @Autowired
    private UserProfileMapper userProfileMapper;

    public UserServiceImpl(UserProfileMapper userProfileMapper) {
        this.userProfileMapper = userProfileMapper;
    }

    @Override
    public void register(UserDTO userProfile) {
        boolean duplIdResult = isDuplicateId(userProfile.getUserId());

        // 아이디 중복 확인
        if(duplIdResult){
            throw new DuplicateIdException("중복된 아이디입니다.");
        }

        // 중복 통과 -> 현재 날짜 설정
        userProfile.setCreateTime(new Date());
        // 비밀번호 SHA-256으로 암호화
        userProfile.setPassword(encryptionSHA256(userProfile.getPassword()));
        int insertCount = userProfileMapper.register(userProfile);
        if(insertCount != 1){
            log.error("insertMember Error! {}", userProfile);
            throw new RuntimeException(
                    "insert User Error : 회원가입 메서드를 확인해주세요 \n" + "Param : " + userProfile
            );
        }
    }

    @Override
    public UserDTO login(String id, String password) {
        String cryptPassword = encryptionSHA256(password);
        UserDTO memberInfo = userProfileMapper.findByUserIdAndPassword(id, cryptPassword);
        return memberInfo;
    }

    @Override
    public boolean isDuplicateId(String id) {
        return userProfileMapper.idCheck(id) == 1;
    }

    @Override
    public UserDTO getUserInfo(String userId) {
        return userProfileMapper.getUserProfile(userId);
    }

    @Override
    public void updatePassword(String id, String beforPassword, String afterPassword) {
        // 바꾸기 전 패스워드를 암호화
        String cryptPassword = encryptionSHA256(beforPassword);
        UserDTO memberInfo = userProfileMapper.findByIdAndPassword(id, cryptPassword);

        if(memberInfo != null){
            memberInfo.setPassword(encryptionSHA256(afterPassword));
            int insertCount = userProfileMapper.updatePassword(memberInfo);
        } else {
            log.error("updatePassword Error! {}", memberInfo);
            throw new IllegalArgumentException("updatePasswrod ERROR! 비밀번호 변경 메서드를 확인해주세요\n" + "Params : " + memberInfo);
        }
    }

    @Override
    public void deleteId(String id, String password) {
        String cryptPassword = encryptionSHA256(password);
        UserDTO memberInfo = userProfileMapper.findByIdAndPassword(id, cryptPassword);

        if(memberInfo != null){
            userProfileMapper.deleteUserProfile(memberInfo.getUserId());
        }  else{
            log.error("deleteId Error! {}", memberInfo);
            throw new RuntimeException("deleteId ERROR! id 삭제 메서드를 확인해주세요\n" + "Params : " + memberInfo);
        }
    }
}
