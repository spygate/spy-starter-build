package spy.project.objs;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import spy.project.exceptions.codes.IErrorCode;

@Data
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorResponse {
    private String returnCode;
    private String errorMsg;

    public ErrorResponse(IErrorCode errorCode) {
        this.returnCode = errorCode.code();
        this.errorMsg = errorCode.message(null);
    }

    public ErrorResponse(IErrorCode errorCode, String messageAppend) {
        this.returnCode = errorCode.code();
        this.errorMsg = errorCode.message(messageAppend);
    }

}
