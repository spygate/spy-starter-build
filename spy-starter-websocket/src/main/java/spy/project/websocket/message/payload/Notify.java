package spy.project.websocket.message.payload;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Notify {
    private String mid;
    private String type;
    private String message; //发送信息json格式
}
