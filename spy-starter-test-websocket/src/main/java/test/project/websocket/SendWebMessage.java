package test.project.websocket;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.socket.TextMessage;
import spy.project.utils.JsonUtils;
import spy.project.websocket.message.payload.Notify;
import spy.project.websocket.service.WebMessageSender;
import spy.project.websocket.service.WebSessionHolder;

@RestController
@RequestMapping("/send")
public class SendWebMessage {

    @Autowired
    private WebMessageSender webMessageSender;

    @Autowired
    private WebSessionHolder webSessionHolder;

    @PostMapping("/msg/{id}")
    public Notify sendMessage(@PathVariable String id, @RequestBody Notify notify) throws JsonProcessingException {
        webMessageSender.sendMessageById(id, new TextMessage(JsonUtils.toJson(notify)));
        return notify;
    }

    @GetMapping("/online")
    public Long onlineNumber() {
        return webSessionHolder.getOnline();
    }

}
