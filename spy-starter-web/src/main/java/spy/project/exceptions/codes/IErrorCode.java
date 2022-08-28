package spy.project.exceptions.codes;

import org.springframework.http.HttpStatus;

public interface IErrorCode {
    String code();
    String message(String value);
    HttpStatus httpStatus();
}
