package com.namoo.boardserver.utils;


import lombok.extern.log4j.Log4j2;

import java.security.MessageDigest;

@Log4j2
public class SHA256Util {

    // 해시 알고리즘 이름 지정
    public static final String ENCRYPTION_TYPE = "SHA-256";

    public static String encryptionSHA256(String str){
        String SHA = null;

        // 자바 제공 표준 암호화 라이브러리
        MessageDigest sh;
        try {
            // 해시 알고리즘 객체 생성
            sh = MessageDigest.getInstance(ENCRYPTION_TYPE);

            // 입력 문자열을 바이트 변환해 업데이트
            sh.update(str.getBytes());

            // 해싱 결과 배열 얻기
            byte[] byteData = sh.digest();

            // 바이트 배열을 16진수 문자열로 변환
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < byteData.length; i++) {
                sb.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            SHA = sb.toString();
        } catch (Exception e) {
            log.error("encryptSHA256.ERROR : {} ", e.getMessage());
        }

        return SHA;
    }
}
