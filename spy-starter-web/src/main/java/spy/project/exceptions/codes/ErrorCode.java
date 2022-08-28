package spy.project.exceptions.codes;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.OK;

public enum ErrorCode implements IErrorCode {
    SUC0000("SUC0000", "成功", OK),
    ERR0001("ERR0001", "服务异常", INTERNAL_SERVER_ERROR),
    ERR0002("ERR0002", "请求参数异常", INTERNAL_SERVER_ERROR),
    ERR0003("ERR0003", "请求无权限访问", INTERNAL_SERVER_ERROR),
    ;


    private String code;
    private String message;
    private HttpStatus httpStatus;

    ErrorCode(String code, String message, HttpStatus httpStatus) {
        this.code = code;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public String code() {
        return this.code;
    }

    public String message(String value) {
        if(StringUtils.isEmpty(value)) {
            return String.format("%s-%s", this.code, this.message);
        }
        return String.format("%s-%s:%s", this.code, this.message, value);
    }

    public HttpStatus httpStatus() {
        return this.httpStatus;
    }
}
