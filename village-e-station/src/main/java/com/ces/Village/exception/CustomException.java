package com.ces.village.exception;

import com.ces.village.constant.ErrorCodeEnum;
import lombok.Getter;

/**
 * 自定义业务异常类
 */
@Getter
public class CustomException extends RuntimeException {
    private ErrorCodeEnum errorCode;

    public CustomException(String message) {
        super(message);
    }

    public CustomException(ErrorCodeEnum errorCode) {
        super(errorCode.getMsg());
        this.errorCode = errorCode;
    }

}
