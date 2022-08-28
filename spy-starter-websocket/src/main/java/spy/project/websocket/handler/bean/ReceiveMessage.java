package spy.project.websocket.handler.bean;

import lombok.Data;

@Data
public class ReceiveMessage {
    private String key;     //消息类型
    private String subkey;  //消息子类
    private String message; //消息内容
}
