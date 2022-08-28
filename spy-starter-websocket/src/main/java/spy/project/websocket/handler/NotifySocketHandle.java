package spy.project.websocket.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.*;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import spy.project.utils.JsonUtils;
import spy.project.websocket.handler.bean.Consts;
import spy.project.websocket.handler.bean.ReceiveMessage;
import spy.project.websocket.service.WebSessionHolder;

@Slf4j
public class NotifySocketHandle extends AbstractWebSocketHandler {

    @Autowired
    private WebSessionHolder webSessionHolder;

    @Autowired
    private DispatchFunction dispatchFunction;

    //记录用户session
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String userId = (String) session.getAttributes().get(Consts.TOKEN_USER);
        log.info("websocket connect: {}", userId);
        webSessionHolder.save(userId, session);
    }

    //处理文本消息
    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            String payload = message.getPayload();
            log.info("websocket reveive message: {}", payload);
            ReceiveMessage msg = JsonUtils.toObject(payload, ReceiveMessage.class);
            TextMessage ret = new TextMessage(dispatchFunction.dispatch(msg));
            session.sendMessage(ret);
        } catch (Exception e) {
            session.sendMessage(new TextMessage("消息格式错误"));
            log.error("消息格式错误: {}", message.getPayload());
        }

    }


    //处理心跳消息
    @Override
    protected void handlePongMessage(WebSocketSession session, PongMessage message) throws Exception {
        log.info("websocket心跳检测");
        session.sendMessage(message);
    }

    //处理异常
    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        log.error("websocket链接异常: session = {}, exception = {}", session, exception);
    }

    //处理连接关闭
    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        log.info("websocket连接关闭");
        webSessionHolder.remove(session);
    }


}
