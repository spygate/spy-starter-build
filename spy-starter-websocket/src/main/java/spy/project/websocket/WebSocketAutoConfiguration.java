package spy.project.websocket;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import org.springframework.web.socket.server.standard.ServletServerContainerFactoryBean;
import spy.project.websocket.handler.NotifySocketHandle;
import spy.project.websocket.handler.NotifySocketInterceptor;
import spy.project.websocket.service.WebMessageSender;
import spy.project.websocket.service.WebSessionHolder;

@Configuration
@EnableConfigurationProperties({
        WebSocketProperties.class
})
@EnableWebSocket
@ConditionalOnProperty(name = "spy.websocket.enable", havingValue = "true", matchIfMissing = false)
public class WebSocketAutoConfiguration implements WebSocketConfigurer {

    /**
     * sockJs
     *  http://host:port/{endpoint}/{server-id}/{session-id}/websocket
     *  ws://host:port/{endpoint}/websocket
     *
     * 不使用sockJs
     *  ws://host:port/{endpoint}
     */
    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        WebSocketProperties properties = webSocketProperties();
        properties.getEndPoints().forEach(endpoint -> {
            registry.addHandler(notifySocketHandle(), endpoint).addInterceptors(notifySocketInterceptor()).setAllowedOrigins("*");
            registry.addHandler(notifySocketHandle(), endpoint).addInterceptors(notifySocketInterceptor()).setAllowedOrigins("*").withSockJS();
        });
    }

    @Bean
    public ServletServerContainerFactoryBean servletServerContainerFactoryBean() {
        WebSocketProperties properties = webSocketProperties();
        ServletServerContainerFactoryBean containerFactoryBean = new ServletServerContainerFactoryBean();
        containerFactoryBean.setMaxBinaryMessageBufferSize(properties.getBufferSize());//512000
        containerFactoryBean.setMaxTextMessageBufferSize(properties.getBufferSize());//512000
        containerFactoryBean.setMaxSessionIdleTimeout(properties.getIdleTimeout());//3600*2
        return containerFactoryBean;
    }


    @Bean
    public WebSocketProperties webSocketProperties() {
        return new WebSocketProperties();
    }

    @Bean
    public NotifySocketInterceptor notifySocketInterceptor() {
        return new NotifySocketInterceptor();
    }

    @Bean
    public NotifySocketHandle notifySocketHandle() {
        return new NotifySocketHandle();
    }

    @Bean
    public WebSessionHolder webSessionHolder() {
        return new WebSessionHolder();
    }

    @Bean
    public WebMessageSender webMessageSender() {
        return new WebMessageSender();
    }

}
