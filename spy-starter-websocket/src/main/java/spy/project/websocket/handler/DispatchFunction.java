package spy.project.websocket.handler;

import spy.project.websocket.handler.bean.ReceiveMessage;

public interface DispatchFunction {
    String dispatch(ReceiveMessage message);
}
