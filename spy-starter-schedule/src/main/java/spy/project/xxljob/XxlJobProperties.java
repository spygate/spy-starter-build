package spy.project.xxljob;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "spy.schedule")
public class XxlJobProperties {
    private Boolean enable;
    private String adminAddress;//调度中心地址
    private String accessToken;//通信token
    private String executorAppname;//执行器名称
    private String executorAddress;//执行器地址，与ip二选一
    private String executorIp;//执行器ip
    private Integer executorPort;//执行器端口
    private String executorLogpath;//日志路径
    private Integer executorLogretentionDays;//保存日志天数
}
