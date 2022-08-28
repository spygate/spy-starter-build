package test.project.controller;

import cn.hutool.jwt.JWTUtil;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/token")
public class TokenController {

    @GetMapping("/get/{userId}")
    public String genToken(@PathVariable("userId") String userId) {
        Map<String, Object> p = new HashMap<>();
        p.put("userId", userId);
        return JWTUtil.createToken(p, "1234567890123456".getBytes());
    }

}
