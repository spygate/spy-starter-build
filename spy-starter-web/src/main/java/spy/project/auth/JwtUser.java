package spy.project.auth;

import lombok.Data;

@Data
public class JwtUser implements JwtUserInterface {
    private String userId;
    private String userName;
    private String userMobile;
}
