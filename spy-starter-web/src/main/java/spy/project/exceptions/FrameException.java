package spy.project.exceptions;

import lombok.Data;
import spy.project.exceptions.codes.IErrorCode;

/**
 * 框架异常
 */
@Data
public class FrameException extends RuntimeException {

    protected IErrorCode errorCode;

    public FrameException(IErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public FrameException(IErrorCode errorCode, String messageAppend) {
        this.errorCode = errorCode;
        this.errorCode.message(messageAppend);
    }
}
