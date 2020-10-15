package indi.eiriksgata.dice.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务统一异常
 *
 * @author Snake
 * @date 2019/11/14
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class CommonException extends RuntimeException {

    private static final long serialVersionUID = -6301630219349537830L;

    private Integer errCode;
    private String errMsg;

    public CommonException(Integer errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public CommonException(String errMsg) {
        this.errCode = ExceptionEnum.UNKNOWN.getErrCode();
        this.errMsg = errMsg;
    }

    public CommonException(ExceptionEnum exceptionEnum) {
        this.errCode = exceptionEnum.getErrCode();
        this.errMsg = exceptionEnum.getErrMsg();
    }

    public CommonException(ExceptionEnum exceptionEnum, Throwable cause) {
        super(cause);
        this.errCode = exceptionEnum.getErrCode();
        this.errMsg = exceptionEnum.getErrMsg();
    }
}
