package spy.project.log;

import org.springframework.util.StopWatch;

public class LogThreadLocal {
    public static final ThreadLocal<StopWatch> stopWatchThreadLocal = new ThreadLocal<>();
    public static final ThreadLocal<String> seqLogWatchThreadLocal = new ThreadLocal<>();
    public static final ThreadLocal<Boolean> ignore = new ThreadLocal<>();
}
