package spy.project.auth;

public interface Jwt {
    JwtUser parseJwtToken(String token);
    String generateJwtToken(JwtUser jwtUser);
}
