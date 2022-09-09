package spy.project.xxljob;

import com.xxl.job.core.executor.impl.XxlJobSpringExecutor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({
        XxlJobProperties.class
})
@ConditionalOnProperty(name = "spy.schedule.enable", havingValue = "true", matchIfMissing = false)
public class XxlJobAutoConfiguration {

    @Bean
    public XxlJobSpringExecutor xxlJobExecutor(XxlJobProperties props) {
        XxlJobSpringExecutor xxlJobSpringExecutor = new XxlJobSpringExecutor();
        xxlJobSpringExecutor.setAdminAddresses(props.getAdminAddress());
        xxlJobSpringExecutor.setAccessToken(props.getAccessToken());
        xxlJobSpringExecutor.setAppname(props.getExecutorAppname());
        xxlJobSpringExecutor.setAddress(props.getExecutorAddress());
        xxlJobSpringExecutor.setIp(props.getExecutorIp());
        xxlJobSpringExecutor.setPort(props.getExecutorPort());
        xxlJobSpringExecutor.setLogPath(props.getExecutorLogpath());
        xxlJobSpringExecutor.setLogRetentionDays(props.getExecutorLogretentionDays());
        return xxlJobSpringExecutor;
    }

}
