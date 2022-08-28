package spy.project.web;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.EnumerablePropertySource;
import org.springframework.core.env.Environment;
import org.springframework.core.env.MutablePropertySources;

import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * 打印启动参数
 */
@Slf4j
public class WebEnvGetterPrinter implements EnvironmentAware {

    private ConfigurableEnvironment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = (ConfigurableEnvironment) environment;
    }

    public void print() {
        MutablePropertySources propertySources = this.environment.getPropertySources();
        Map<String, String> props = StreamSupport.stream(propertySources.spliterator(), false)
                .filter(ps -> ps instanceof EnumerablePropertySource)
                .map(ps -> ((EnumerablePropertySource)ps).getPropertyNames())
                .flatMap(Arrays::stream)
                .distinct()
                .collect(Collectors.toMap(Function.identity(), this.environment::getProperty));
        int max = props.keySet().stream().max(Comparator.comparingInt(String::length)).orElse("").length();
        log.info("{}", String.join("", Collections.nCopies(100, "#")));
        props.keySet().stream().sorted()
                .forEach(k -> {
                    int i = max - k.length() + 20;
                    String join = String.join("", Collections.nCopies(i, " "));
                    log.info("{}{}{}", k, join, props.get(k));
                });
        log.info("{}", String.join("", Collections.nCopies(100, "#")));
    }

}
