package spy.project.exceptions;

import spy.project.exceptions.codes.IErrorCode;

/**
 * 业务异常
 */
public class BusinessException extends FrameException {

    public BusinessException(IErrorCode errorCode) {
        super(errorCode);
    }

}
