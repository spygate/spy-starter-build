package spy.project.config;

import feign.*;
import feign.codec.Encoder;
import feign.codec.ErrorDecoder;
import feign.form.spring.SpringFormEncoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import spy.project.auth.TokenThreadLocal;
import spy.project.exceptions.FrameException;
import spy.project.exceptions.codes.ErrorCode;
import spy.project.objs.ErrorResponse;
import spy.project.utils.JsonUtils;

import java.nio.charset.Charset;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;


@Slf4j
@Configuration
@ConditionalOnClass({Feign.class})
@ConditionalOnProperty(value = "spy.web.feignEnable", havingValue = "true", matchIfMissing = false)
public class FeignConfiguration {

    @Autowired
    private ObjectFactory<HttpMessageConverters> messageConverters;

    @Bean
    public Encoder feignFormEncoder() {
        return new SpringFormEncoder(new SpringEncoder(messageConverters));
    }

    @Bean
    public Logger.Level feignLog() {
        return Logger.Level.FULL;
    }

    @Bean
    public FeignHeaderInterceptor feignHeaderInterceptor() {
        return new FeignHeaderInterceptor();
    }

    @Bean
    public FeignErrorDecoder feignErrorDecoder() {
        return new FeignErrorDecoder();
    }

    private class FeignHeaderInterceptor implements RequestInterceptor {

        @Override
        public void apply(RequestTemplate requestTemplate) {
            if(TokenThreadLocal.tokenThreadLocal.get() != null) {
                requestTemplate.header(AUTHORIZATION, TokenThreadLocal.tokenThreadLocal.get());
            }
        }
    }

    private class FeignErrorDecoder implements ErrorDecoder {

        @Override
        public Exception decode(String key, Response response) {
            try {
                Response.Body body = response.body();
                if(body == null) {
                    return new FrameException(ErrorCode.ERR0001);
                }
                String bodyStr = Util.toString(body.asReader(Charset.forName("UTF-8")));
                ErrorResponse errorResponse = JsonUtils.toObject(bodyStr, ErrorResponse.class);
                if(errorResponse != null) {
                    return new FrameException(ErrorCode.ERR0001, errorResponse.getReturnCode() + "-" + errorResponse.getErrorMsg());
                }

            } catch (Exception e) {
                log.info("Feign请求异常: method = {}, response = {}", key, response, e);
            }
            return new FrameException(ErrorCode.ERR0001);
        }
    }



}
