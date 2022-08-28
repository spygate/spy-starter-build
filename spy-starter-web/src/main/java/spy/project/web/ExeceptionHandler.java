package spy.project.web;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spy.project.exceptions.BusinessException;
import spy.project.exceptions.FrameException;
import spy.project.exceptions.codes.ErrorCode;
import spy.project.objs.ErrorResponse;

import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

@Slf4j
@RestControllerAdvice
public class ExeceptionHandler {

    @ExceptionHandler(value = {FrameException.class, BusinessException.class})
    public ResponseEntity<ErrorResponse> handleCustomException(FrameException e, HttpServletResponse response) {
        log.error("{} custom server exception {}: code={}, message={}, stacktrace={}",
                String.join("", Collections.nCopies(10, "#")),
                String.join("", Collections.nCopies(10, "#")),
                e.getErrorCode().code(),
                e.getErrorCode().message(null),
                ExceptionUtils.getStackTrace(e)
                );
        if(e.getErrorCode().httpStatus() != null) {
            return ResponseEntity.status(e.getErrorCode().httpStatus()).body(new ErrorResponse(e.getErrorCode()));
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(e.getErrorCode()));
        }

    }

    @ExceptionHandler(value = {Exception.class})
    public ResponseEntity<ErrorResponse> defaultHandle(Exception e, HttpServletResponse response) {
        log.error("{} custom server exception {}: stacktrace={}",
                String.join("", Collections.nCopies(10, "#")),
                String.join("", Collections.nCopies(10, "#")),
                ExceptionUtils.getStackTrace(e)
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ErrorResponse(ErrorCode.ERR0001));
    }

}
