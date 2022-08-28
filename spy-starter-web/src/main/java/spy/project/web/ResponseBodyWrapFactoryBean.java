package spy.project.web;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.web.method.support.HandlerMethodReturnValueHandler;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestResponseBodyMethodProcessor;

import java.util.ArrayList;
import java.util.List;

public class ResponseBodyWrapFactoryBean implements InitializingBean {
    private final RequestMappingHandlerAdapter adapter;

    public ResponseBodyWrapFactoryBean(RequestMappingHandlerAdapter adapter) {
        this.adapter = adapter;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        List<HandlerMethodReturnValueHandler> returnValueHandlers = adapter.getReturnValueHandlers();
        if (returnValueHandlers.size() > 0) {
            // 将内置的返回值处理器进行替换
            List<HandlerMethodReturnValueHandler> handlers = new ArrayList<>(returnValueHandlers);
            decorateHandlers(handlers);
            adapter.setReturnValueHandlers(handlers);
        }
    }

    /**
     * 将所有RequestResponseBodyMethodProcessor返回值处理器替换为自定义的返回值处理器
     */
    private void decorateHandlers(List<HandlerMethodReturnValueHandler> handlers) {
        for (HandlerMethodReturnValueHandler handler : handlers) {
            if (handler instanceof RequestResponseBodyMethodProcessor) {
                // 替换为自定义返回值处理器
                ReturnValueHandler decorator = new ReturnValueHandler(handler);
                int index = handlers.indexOf(handler);
                handlers.set(index, decorator);
                break;
            }
        }
    }
}
