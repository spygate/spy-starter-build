package spy.project.websocket;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@Data
@ConfigurationProperties(prefix = "spy.websocket")
public class WebSocketProperties {

    private Boolean enable;
    private Integer bufferSize; //缓存区大小
    private Long idleTimeout; //空闲时间
    private List<String> endPoints; //端点路径

}
