package com.namoo.boardserver.aop;

import com.namoo.boardserver.utils.SessionUtil;
import jakarta.servlet.http.HttpSession;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
@Aspect // AOP 클래스임을 표시
@Order(Ordered.LOWEST_PRECEDENCE)   // 낮은 순위의 AOP로 실행
@Log4j2
public class LoginCheckAspect {

    // Around : 어드바이스에서 실제 호출 전/후에 로그인 체크 수행

    // @annotation(com.namoo.boardserver.aop.LoginCheck) : @LoginCheck가 붙은 메서드에서 이 Advice 실행
    // @annotation(loginCheck) : Advice 매개 변수로 어노테이션 객체 자체가 들어감
    @Around("@annotation(com.namoo.boardserver.aop.LoginCheck) && @annotation(loginCheck)")
    public Object adminLoginCheck(ProceedingJoinPoint proceedingJoinPoint, LoginCheck loginCheck) throws Throwable {

        // 현재 요청의 세션을 가져옴
        HttpSession session = (HttpSession) ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest().getSession();
        String id = null;
        int idIndex = 0;

        // 세션에서 로그인 한 사용자 ID 가져옴
        String userType = loginCheck.type().toString();
        switch (userType) {
            case "ADMIN": {
                id = SessionUtil.getLoginAdminId(session);
                break;
            }
            case "USER": {
                id = SessionUtil.getLoginMemberId(session);
                break;
            }
        }
        // id가 Null이면
        if (id == null) {
            log.debug(proceedingJoinPoint.toString()+ "accountName :" + id);
            throw new HttpStatusCodeException(HttpStatus.UNAUTHORIZED, "로그인한 id값을 확인해주세요.") {};
        }

        // 실행할 메서드의 파라미터 목록을 가져옴
        Object[] modifiedArgs = proceedingJoinPoint.getArgs();

        // 첫 번째 매개 변수를 로그인 한 ID로 변경
        if(proceedingJoinPoint.getArgs()!=null)
            modifiedArgs[idIndex] = id;

        // 원래 컨트롤러 메서드를 호출 -> 변경된 매개 변수가 들어가 있음
        return proceedingJoinPoint.proceed(modifiedArgs);
    }
}
