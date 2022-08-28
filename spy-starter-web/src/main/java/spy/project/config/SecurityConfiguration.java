package spy.project.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import spy.project.auth.CurrentUserAspect;
import spy.project.auth.JwtService;
import spy.project.auth.JwtTokenFilter;

@Slf4j
@Configuration
public class SecurityConfiguration implements WebMvcConfigurer {

    private GlobalWebProperties globalWebProperties;
    private JwtService jwtService;

    public SecurityConfiguration(@Nullable GlobalWebProperties webProperties, @Nullable JwtService jwtService) {
        this.globalWebProperties = webProperties;
        this.jwtService = jwtService;
    }

    @Bean
    public FilterRegistrationBean<JwtTokenFilter> jwtTokenFilter() {
        FilterRegistrationBean<JwtTokenFilter> registrationBean = new FilterRegistrationBean<>();
        JwtTokenFilter jwtTokenFilter = new JwtTokenFilter();
        jwtTokenFilter.setJwtService(jwtService);
        jwtTokenFilter.setProperties(globalWebProperties);
        registrationBean.setFilter(jwtTokenFilter);
        registrationBean.addUrlPatterns("/*");
        registrationBean.setName("jwtTokenFilter");
        registrationBean.setOrder(5);
        return registrationBean;
    }

    @Bean
    public CurrentUserAspect currentUserAspect() {
        return new CurrentUserAspect();
    }

}
