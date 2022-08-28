package spy.project.log;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.util.StopWatch;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

@Slf4j
public class LogInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        StopWatch sw = new StopWatch();
        sw.start();
        LogThreadLocal.stopWatchThreadLocal.set(sw);
        String path = getPath(request);
        String logpath = String.format("<%s><%s>", getSeq(), path);
        LogThreadLocal.seqLogWatchThreadLocal.set(logpath);

        log.info("ControllerMethod = {}", logpath);
        if(handler instanceof HandlerMethod) {
            LogIgnore ignore = ((HandlerMethod) handler).getMethod().getAnnotation(LogIgnore.class);
            if(ignore == null) {
                LogThreadLocal.ignore.set(false);
                log.info("Request = {}", IOUtils.toString(request.getInputStream()));
            }
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    private String getPath(HttpServletRequest request) {
        String path;
        if(request.getQueryString() == null) {
            path = request.getRequestURI();
        } else {
            path = request.getRequestURI() + "?" + request.getQueryString();
        }
        return path;
    }

    private String getSeq() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}


