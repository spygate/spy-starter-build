package spy.project.auth;

import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.util.Arrays;

@Aspect
@Slf4j
public class CurrentUserAspect {

    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) || @annotation(org.springframework.web.bind.annotation.GetMapping) || @annotation(org.springframework.web.bind.annotation.PostMapping) || @annotation(org.springframework.web.bind.annotation.DeleteMapping) || @annotation(org.springframework.web.bind.annotation.PutMapping)")
    public void currentUser(){}

    @Before("currentUser()")
    public void before(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Arrays.stream(args).forEach(arg -> {
            if(arg instanceof JwtUser) {
                BeanUtil.copyProperties(TokenThreadLocal.jwtUserThreadLocal.get(), arg);
            }
        });
    }

}
