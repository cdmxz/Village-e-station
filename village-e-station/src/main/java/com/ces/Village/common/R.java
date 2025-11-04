package com.ces.village.common;

import com.ces.village.constant.ErrorCodeEnum;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 通用返回结果，服务端响应的数据最终都会封装成此对象
 *
 * @param <T>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class R<T> implements Serializable {

    private Integer code; //返回码
    private String msg; //返回信息
    private T data; //返回数据

    public static <T> R<T> success() {
        R<T> r = new R<>();
        r.code = ErrorCodeEnum.SUCCESS.getCode();
        r.msg = ErrorCodeEnum.SUCCESS.getMsg();
        return r;
    }

    public static <T> R<T> success(T object) {
        R<T> r = new R<>();
        r.code = ErrorCodeEnum.SUCCESS.getCode();
        r.msg = ErrorCodeEnum.SUCCESS.getMsg();
        r.data = object;
        return r;
    }

    public static <T> R<T> error(ErrorCodeEnum errorCodeEnum) {
        R<T> r = new R<>();
        r.msg = errorCodeEnum.getMsg();
        r.code = errorCodeEnum.getCode();
        return r;
    }

    public static <T> R<T> error(int code, String msg) {
        return new R<>(code, msg, null);
    }
}