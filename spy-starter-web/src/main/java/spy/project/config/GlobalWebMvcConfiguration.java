package spy.project.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import spy.project.log.LogInterceptor;
import spy.project.web.ExeceptionHandler;
import spy.project.web.RequestWrapperFilter;



@Configuration
public class GlobalWebMvcConfiguration implements WebMvcConfigurer  {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LogInterceptor()).addPathPatterns("/**");
    }

    @Bean
    public RequestWrapperFilter requestWrapperFilter() {
        return new RequestWrapperFilter();
    }

    @Bean
    public FilterRegistrationBean<RequestWrapperFilter> filterFilterRegistrationBean() {
        FilterRegistrationBean<RequestWrapperFilter> registrationBean = new FilterRegistrationBean<RequestWrapperFilter>();
        RequestWrapperFilter myFilter = requestWrapperFilter();
        registrationBean.setFilter(myFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("requestWrapperFilter");
        registrationBean.setOrder(1);
        return registrationBean;
    }

    @Bean
    public ExeceptionHandler execeptionHandler() {
        return new ExeceptionHandler();
    }


}
