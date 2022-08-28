package spy.project.auth;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import spy.project.config.GlobalWebProperties;
import spy.project.consts.MessageConst;
import spy.project.exceptions.FrameException;
import spy.project.exceptions.codes.ErrorCode;
import spy.project.utils.JsonUtils;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Slf4j
public class JwtService implements Jwt {

    @Autowired
    private GlobalWebProperties globalWebProperties;

    public JwtUser parseJwtToken(String token) {
        JWT jwt = JWTUtil.parseToken(token);
        Boolean keyVerify = jwt.setKey(globalWebProperties.getJwt().getSecret().getBytes()).verify();
        Boolean timeVerify = jwt.validate(0);
        if(!keyVerify || !timeVerify) {
            throw new FrameException(ErrorCode.ERR0001, MessageConst.JWT_TOKEN_NULL);
        }
        try {
            return JsonUtils.toObject(JsonUtils.toJson(jwt.getPayload().getClaimsJson()), JwtUser.class);
        } catch (Exception e) {
            throw new FrameException(ErrorCode.ERR0001, MessageConst.JWT_TOKEN_PARSE);
        }
    }

    public String generateJwtToken(JwtUser jwtUser) {
        try {
            Map jwtPayload = JsonUtils.toObject(JsonUtils.toJson(jwtUser), Map.class);
            LocalDateTime now = LocalDateTime.now();
            jwtPayload.put(JWTPayload.EXPIRES_AT, now.plusSeconds(globalWebProperties.getJwt().getExpireTimeSecond()));
            jwtPayload.put(JWTPayload.ISSUED_AT, now);
            return JWTUtil.createToken(jwtPayload, globalWebProperties.getJwt().getSecret().getBytes());
        } catch (IOException e) {
            throw new FrameException(ErrorCode.ERR0001, MessageConst.JWT_TOKEN_GEN);
        }

    }

}
