package spy.project.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.util.StopWatch;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.method.support.ModelAndViewContainer;
import spy.project.auth.TokenThreadLocal;
import spy.project.log.LogThreadLocal;
import spy.project.objs.Response;
import spy.project.utils.JsonUtils;

@Slf4j
public class ReturnValueHandler implements HandlerMethodReturnValueHandler {

    private final HandlerMethodReturnValueHandler delegate;

    public ReturnValueHandler(HandlerMethodReturnValueHandler delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean supportsReturnType(MethodParameter returnType) {
        return delegate.supportsReturnType(returnType);
    }

    @Override
    public void handleReturnValue(Object returnValue,
                                  MethodParameter returnType,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest) throws Exception {
        // 打印返回值
        StopWatch watch = LogThreadLocal.stopWatchThreadLocal.get();
        if(watch == null) {
            delegate.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
            return;
        }
        watch.stop();
        long ms = watch.getTotalTimeMillis();
        if(!LogThreadLocal.ignore.get()) {
            log.info("Response = {}", JsonUtils.toJson(returnValue));
        }
        log.info("ControllerMethod = {}, Duration = {} ms End", LogThreadLocal.seqLogWatchThreadLocal.get(), ms);
        clear();//释放内存，避免泄漏

        // 如果类或者方法含有不包装注解则忽略包装
        IgnoreResponseWrapper wrapper = returnType.getDeclaringClass().getAnnotation(IgnoreResponseWrapper.class);
        if (wrapper != null) {
            delegate.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
            return;
        }
        wrapper = returnType.getMethodAnnotation(IgnoreResponseWrapper.class);
        if (wrapper != null) {
            delegate.handleReturnValue(returnValue, returnType, mavContainer, webRequest);
            return;
        }

        // 自定义返回格式
        Response response = new Response();
        response.setReturnCode("SUC0000");
        response.setData(returnValue);
        delegate.handleReturnValue(response, returnType, mavContainer, webRequest);
    }

    // 释放线程变量的弱引用，避免内存泄漏
    private void clear() {
        LogThreadLocal.stopWatchThreadLocal.remove();
        LogThreadLocal.seqLogWatchThreadLocal.remove();
        LogThreadLocal.ignore.remove();

        TokenThreadLocal.tokenThreadLocal.remove();
        TokenThreadLocal.jwtUserThreadLocal.remove();
    }
}
