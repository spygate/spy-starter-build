package spy.project.websocket.handler;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;
import spy.project.utils.JsonUtils;
import spy.project.websocket.handler.bean.Consts;
import spy.project.websocket.handler.bean.UserInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;


/**
 * 获取用户token信息，并处理attr属性
 */
@Slf4j
public class NotifySocketInterceptor extends HttpSessionHandshakeInterceptor {

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler, Map<String, Object> attributes) throws Exception {
        String token = getToken(request, response);
        if(!StringUtils.isEmpty(token)) {
            String userId = parseToken(token);
            if(!StringUtils.isEmpty(userId)) {
                attributes.put(Consts.TOKEN_USER, userId);
                return super.beforeHandshake(request, response, wsHandler, attributes);
            }
        }
        return false;
    }

    private String parseToken(String token) {
        try {
            JWT jwt = JWTUtil.parseToken(token);
            return JsonUtils.toObject(JsonUtils.toJson(jwt.getPayload().getClaimsJson()), UserInfo.class).getUserId();
        } catch (Exception e) {
            log.error("token 解析错误: {}", token);
            return null;
        }
    }

    private String getToken(ServerHttpRequest request, ServerHttpResponse response) {
        HttpServletRequest httpServletRequest = ((ServletServerHttpRequest) request).getServletRequest();
        String token = httpServletRequest.getParameter("token");
        if(StringUtils.isEmpty(token)) {
            HttpServletResponse httpServletResponse = ((ServletServerHttpResponse) response).getServletResponse();
            token = httpServletRequest.getHeader("Sec-WebSocket-Protocol");
            httpServletResponse.addHeader("Sec-WebSocket-Protocol", token);
        }
        return token;
    }

}
