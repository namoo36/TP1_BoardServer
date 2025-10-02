    package com.namoo.boardserver.utils;

    import jakarta.servlet.http.HttpSession;

    // 로그인한 사용자를 HttpSession에 저장하고 꺼내는 헬퍼
    public class SessionUtil {
        // 세션에 값을 넣을 때 쓸 key 값
        private static final String LOGIN_MEMBER_ID = "LOGIN_MEMBER_ID";
        private static final String LOGIN_ADMIN_ID = "LOGIN_ADMIN_ID";

        // 생성자 private 처리 -> 객체 생성 막음
        private SessionUtil() {
        }

        // 세션에서 "LOGIN_MEMBER_ID" 꺼내오기
        public static String getLoginMemberId(HttpSession session) {
            return (String) session.getAttribute(LOGIN_MEMBER_ID);
        }
        // 로그인 성공 시 세션에 "LOGIN_MEMBER_ID" = id 저장
        public static void setLoginMemberId(HttpSession session, String id) {
            session.setAttribute(LOGIN_MEMBER_ID, id);
        }
        // 세션에서 "LOGIN_ADMIN_ID" 꺼내오기
        public static String getLoginAdminId(HttpSession session) {
            return (String) session.getAttribute(LOGIN_ADMIN_ID);
        }
        // 로그인 성공 시 세션에 "LOGIN_ADMIN_ID" = id 저장
        public static void setLoginAdminId(HttpSession session, String id) {
            session.setAttribute(LOGIN_ADMIN_ID, id);
        }
        // 세션 자체 무효화 -> 로그아웃
        public static void clear(HttpSession session) {
            session.invalidate();
        }
    }