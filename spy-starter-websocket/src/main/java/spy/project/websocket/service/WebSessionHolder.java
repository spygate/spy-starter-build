package spy.project.websocket.service;

import org.springframework.web.socket.WebSocketSession;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 处理用户链接的session
 */
public class WebSessionHolder {

    //在线连接数
    private final AtomicLong online = new AtomicLong();

    //userid - session, 支持一个用户多端连接
    private final Map<String, Set<WebSocketSession>> webSocketSessionMap = new ConcurrentHashMap<>();

    public List<WebSocketSession> getAll() {
        List<WebSocketSession> sessions = new ArrayList<>();
        webSocketSessionMap.forEach((key, item) -> {
            item.stream().filter(Objects::nonNull)
                    .forEach(s -> {
                        sessions.add(s);
                    });
        });
        return sessions;
    }

    public Set<WebSocketSession> getOrNull(String id) {
        return Optional.ofNullable(webSocketSessionMap.get(id)).orElse(null);
    }

    public void save(String id, WebSocketSession session) {
        Set<WebSocketSession> sessionSet = webSocketSessionMap.get(id) == null ? new HashSet() : webSocketSessionMap.get(id);
        sessionSet.add(session);
        webSocketSessionMap.put(id, sessionSet);
        online.addAndGet(1L);
    }

    public void remove(WebSocketSession session) {
        webSocketSessionMap.forEach((key, item) -> {
            if(item.contains(session)) {
                item.remove(session);
                online.decrementAndGet();
            }
        });
    }

    public Long getOnline() {
        return online.get();
    }

}
