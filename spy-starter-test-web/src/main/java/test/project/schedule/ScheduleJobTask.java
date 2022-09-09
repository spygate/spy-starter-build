package test.project.schedule;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ScheduleJobTask {

    @XxlJob("simpleJob")
    public ReturnT<String> simpleJob(String params) {
        log.info("do simple job ok");
        return ReturnT.SUCCESS;
    }

}
