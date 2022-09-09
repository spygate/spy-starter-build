package spy.project.auth;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import spy.project.config.GlobalWebProperties;
import spy.project.exceptions.FrameException;
import spy.project.exceptions.codes.ErrorCode;
import spy.project.objs.Response;
import spy.project.utils.JsonUtils;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static org.springframework.util.MimeTypeUtils.APPLICATION_JSON_VALUE;

@Slf4j
@Data
public class JwtTokenFilter extends OncePerRequestFilter {

    private JwtService jwtService;

    private GlobalWebProperties properties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if(isSkipPahts(request)) {
            TokenThreadLocal.jwtUserThreadLocal.remove();
            TokenThreadLocal.tokenThreadLocal.remove();
            filterChain.doFilter(request, response);
            return;
        }

        try {
            String header = request.getHeader("Authorization");
            if(StringUtils.isNotBlank(header) && header.startsWith("Bearer ")) {
                log.info("token = {}", header);
                String jwtToken = header.split(" ")[1];

                JwtUser jwtUser = jwtService.parseJwtToken(jwtToken);
                log.info("jwtUser = {}", jwtUser);

                TokenThreadLocal.jwtUserThreadLocal.set(jwtUser);
                TokenThreadLocal.tokenThreadLocal.set("Bearer " + jwtToken);

                filterChain.doFilter(request, response);
            } else {
                throw new FrameException(ErrorCode.ERR0003);
            }
        } catch (Exception e) {
            log.info("token error", e);
            printWeb(response);
        }
    }

    private void printWeb(HttpServletResponse response) throws IOException {
        response.setContentType(APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.setStatus(500);
        Response res = new Response(ErrorCode.ERR0003.code(), ErrorCode.ERR0003.message(null));
        response.getWriter().println(JsonUtils.toJson(res));
    }

    /**
     * 格式支持：
     *   /xxx/**
     *   /xxx/yyy
     */
    private boolean isSkipPahts(HttpServletRequest request) {
        String uri = request.getRequestURI();
        List<String> skips = properties.getSkipPath().stream().map(v -> {
            if(v.endsWith("/**")) {
                if(v.equals("/**")) {
                    return null;
                } else {
                    return v.substring(0, v.length() - 3);
                }
            }
            return v;
        }).filter(Objects::nonNull).collect(Collectors.toList());
        //默认放行路径
        skips.add("/favicon.ico");
        skips.add("/swagger-ui.html");
        skips.add("/webjars");
        skips.add("/swagger-resources");
        skips.add("/v2");
        skips.add("/actuator");
        skips.add("/run");//xxl-job
        for (String str: skips) {
            if(uri.startsWith(str)) {
                return true;
            }
        }
        return false;
    }


}
