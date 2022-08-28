package test.project.web.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;
import spy.project.auth.JwtService;
import spy.project.auth.JwtUser;
import spy.project.sequence.Sequence;
import spy.project.web.IgnoreResponseWrapper;

@Api("token测试")
@RestController
@RequestMapping("/token")
@Slf4j
public class TokenController {

    @Autowired
    private JwtService jwtService;

    @Autowired
    @Qualifier("NanoId")
    private Sequence sequence;

    @ApiOperation("获取token")
    @GetMapping("/get/{userId}/{mobile}")
    public String genToken(@PathVariable("userId") String userId, @PathVariable("mobile") String mobile) {
        JwtUser jwtUser = new JwtUser();
        jwtUser.setUserId(userId);
        jwtUser.setUserName(userId);
        jwtUser.setUserMobile(mobile);
        return "Bearer" + jwtService.generateJwtToken(jwtUser);
    }

    @ApiOperation("获取token")
    @GetMapping("/getrandom")
    @IgnoreResponseWrapper
    public String genToken() {
        JwtUser jwtUser = new JwtUser();
        jwtUser.setUserId(sequence.getNext());
        return "Bearer" + jwtService.generateJwtToken(jwtUser);
    }


}
