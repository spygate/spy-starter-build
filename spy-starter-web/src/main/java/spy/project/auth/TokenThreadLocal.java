package spy.project.auth;

public class TokenThreadLocal {
    public static final InheritableThreadLocal<String> tokenThreadLocal = new InheritableThreadLocal<>();
    public static final InheritableThreadLocal<JwtUser> jwtUserThreadLocal = new InheritableThreadLocal<>();
}
