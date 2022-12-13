package spy.project.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import spy.project.auth.JwtService;
import spy.project.cache.Cache;
import spy.project.cache.RedisCache;
import spy.project.lock.Lock;
import spy.project.lock.RedisLock;
import spy.project.sequence.*;
import spy.project.web.ResponseBodyWrapFactoryBean;
import spy.project.web.WebEnvGetterPrinter;

@Configuration
@EnableConfigurationProperties({
        GlobalWebProperties.class,
        GlobalWebProperties.JwtProperties.class,
        GlobalWebProperties.SequenceProperties.class,
        GlobalWebProperties.LockProperties.class
})
@EnableJpaRepositories(basePackages = {"spy.project.db"})
@EntityScan(basePackages = {"spy.project.db"})
@EnableAspectJAutoProxy(exposeProxy = true)
@Import({GlobalWebMvcConfiguration.class, FeignConfiguration.class, SecurityConfiguration.class})
public class GlobalWebAutoConfiguration {

    @Bean
    public WebEnvGetterPrinter webEnvGetterPrinter() {
        return new WebEnvGetterPrinter();
    }

    @Bean
    public GlobalWebRunner globalWebRunner() {
        return new GlobalWebRunner();
    }

    @Bean
    public ResponseBodyWrapFactoryBean responseBodyWrapFactoryBean(@Nullable RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
        return new ResponseBodyWrapFactoryBean(requestMappingHandlerAdapter);
    }


    @Bean
    @ConditionalOnProperty(value = "spy.web.jwt.enable", havingValue = "true", matchIfMissing = false)
    public JwtService jwtService() {
        return new JwtService();
    }

    @Bean(name = "UUID")
    @ConditionalOnProperty(value = "spy.web.sequence.enable", havingValue = "true", matchIfMissing = false)
    public Sequence UUIDGenerator() {
        return new UUIDGenerator();
    }

    @Bean(name = "NanoId")
    @ConditionalOnProperty(value = "spy.web.sequence.enable", havingValue = "true", matchIfMissing = false)
    public Sequence NanoIdGenerator() {
        return new NanoIdGenerator();
    }

    @Bean(name = "fastId")
    @ConditionalOnProperty(value = "spy.web.sequence.enable-redis", havingValue = "true", matchIfMissing = false)
    public Sequence FastIdGenerator() {
        return new FastIdGenerator();
    }

    @Bean(name = "dbId")
    @ConditionalOnProperty(value = "spy.web.sequence.enable-db", havingValue = "true", matchIfMissing = false)
    public Sequence DbIdGenerator() {
        return new DbIdGenerator();
    }

    @Bean(name = "redisLock")
    @ConditionalOnProperty(value = "spy.web.lock.enable-redis", havingValue = "true", matchIfMissing = false)
    public Lock RedisLock() {
        return new RedisLock();
    }

    @Bean(name = "redisCache")
    @ConditionalOnProperty(value = "spy.web.lock.enable-redis", havingValue = "true", matchIfMissing = false)
    public Cache RedisCache() {
        return new RedisCache();
    }

}
