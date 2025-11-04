package com.ces.village.handler;


import com.ces.village.common.R;
import com.ces.village.constant.ErrorCodeEnum;
import com.ces.village.exception.CustomException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.sql.SQLIntegrityConstraintViolationException;
import java.util.List;

/**
 * 全局异常处理
 */
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@ResponseBody
@Log4j2
public class GlobalExceptionHandler {

    /**
     * 异常处理方法
     *
     * @return
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        log.error("SQL异常处理器exceptionHandler：{}", ex.getMessage());

        if (ex.getMessage().contains("Duplicate entry")) {
            String[] split = ex.getMessage().split(" ");
            String msg = split[2] + "已存在";
            return R.error(ErrorCodeEnum.SERVER_ERROR.getCode(), msg);
        }
        return R.error(ErrorCodeEnum.UNKNOWN_ERROR);
    }

    /**
     * 自定义异常处理方法
     *
     * @return
     */
    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex) {
        log.error("自定义异常处理器exceptionHandler：{}", ex.getMessage());
        return R.error(ex.getErrorCode());
    }

    /**
     * 全局异常处理方法
     * 用于处理 当请求参数不合法时出现的异常
     */
    @ExceptionHandler(value = Exception.class)
    public R<String> exceptionHandler(Exception e) {
        log.error("全局异常处理器exceptionHandler：{}", e.getMessage());
        if (e instanceof BindException ex) {
            List<ObjectError> errors = ex.getAllErrors();
            ObjectError error = errors.get(0);
            String msg = error.getDefaultMessage();
            return R.error(ErrorCodeEnum.PARAM_IS_INVALID.getCode(), msg);
        } else {
            // ToDO 测试环境下，返回给前端的错误信息
            return R.error(ErrorCodeEnum.PARAM_IS_INVALID.getCode(), e.getMessage());
            // TODO 生产环境注释掉上一行
            //            return R.error(ErrorCodeEnum.PARAM_IS_INVALID);
        }
    }

}
