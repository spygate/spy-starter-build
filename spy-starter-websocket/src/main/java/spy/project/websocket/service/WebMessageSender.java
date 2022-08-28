package spy.project.websocket.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 处理服务器推送消息
 */
@Slf4j
public class WebMessageSender {

    @Autowired
    private WebSessionHolder webSessionHolder;

    public <T extends WebSocketMessage> void sendMessageById(String id, T message) {
        Set<WebSocketSession> sessionSet = webSessionHolder.getOrNull(id);
        if(sessionSet != null) {
            sessionSet.stream().filter(Objects::nonNull).forEach(s -> {
                try {
                    s.sendMessage(message);
                } catch (IOException e) {
                    log.error("send message error: {}", e);
                }
            });

        }
    }

    public <T extends WebSocketMessage> void broadcastMessage(T message) {
        List<WebSocketSession> sessionList = webSessionHolder.getAll();
        sessionList.stream()
                    .forEach(session -> {
                        try {
                            session.sendMessage(message);
                        } catch (IOException e) {
                            log.error("broadcast message error: {}", e);
                        }
                    });
    }


}
