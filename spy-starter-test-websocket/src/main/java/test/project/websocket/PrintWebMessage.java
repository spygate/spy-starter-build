package test.project.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import spy.project.utils.JsonUtils;
import spy.project.websocket.handler.DispatchFunction;
import spy.project.websocket.handler.bean.ReceiveMessage;

@Slf4j
@Component
public class PrintWebMessage implements DispatchFunction {
    @Override
    public String dispatch(ReceiveMessage message) {
        try {
            log.info(JsonUtils.toJson(message));
        } catch (JsonProcessingException e) {
            log.info("{}", e);
        }
        return "receive ok";
    }
}
