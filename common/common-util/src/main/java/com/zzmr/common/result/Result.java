package com.zzmr.common.result;

import lombok.Data;

@Data
public class Result<T> {

    // 状态码
    private Integer code;

    // 信息
    private String message;

    // 数据
    private T data;

    // 构造私有化,
    private Result() {

    }

    // 封装返回的是数据
    public static <T> Result<T> build(T body, ResultCodeEnum resultCodeEnum) {
        Result<T> result = new Result<>();

        if (body != null) {
            result.setData(body);
        }
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());

        return result;
    }

    // 成功
    public static <T> Result<T> ok() {
        return build(null, ResultCodeEnum.SUCCESS);
    }

    // 成功-有数据
    public static <T> Result<T> ok(T data) {
        return build(data, ResultCodeEnum.SUCCESS);
    }

    // 失败
    public static <T> Result<T> fail() {
        return build(null, ResultCodeEnum.FAIL);
    }

    // 失败-有数据
    public static <T> Result<T> fail(T data) {
        return build(data, ResultCodeEnum.FAIL);
    }

    public Result<T> message(String msg) {
        this.setMessage(msg);
        return this;
    }

    public Result<T> code(Integer code) {
        this.setCode(code);
        return this;
    }

}
