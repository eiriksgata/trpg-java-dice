package indi.eiriksgata.dice.exception;

import lombok.Getter;

@Getter
public enum ExceptionEnum {

    // 系统错误的指令
    DICE_INSTRUCT_NOT_FOUND(10001, "Not found dice instructs"),


    // system predefine
    WARING(4,"Your operation may be abnormal"),

    ATTONITY(3,"No operation content"),

    VERIFIED(2,"Waiting for your verification"),

    SUCCESS(0,"Operation Success"),

    ERROR(-1,"System Error"),

    UNKNOWN(-2,"Unknown Exception");

    private Integer errCode;
    private String errMsg;

    /**
     * @param errCode 系统号(2bit) + 业务模块（2bit）+ 业务异常（2bit）
     * @param errMsg  异常消息
     */
    ExceptionEnum(Integer errCode, String errMsg) {
        this.errCode = errCode;
        this.errMsg = errMsg;
    }

    public Integer getErrCode() {
        return errCode;
    }

    public String getErrMsg() {
        return errMsg;
    }

    /**
     * find exception enum by err code
     *
     * @param errCode error code
     * @return exception enum
     */
    public static ExceptionEnum getExceptionEnumByCode(Integer errCode) {
        for (ExceptionEnum exceptionEnum : ExceptionEnum.values()) {
            if (exceptionEnum.getErrCode().equals(errCode)) {
                return exceptionEnum;
            }
        }
        return null;
    }
}
