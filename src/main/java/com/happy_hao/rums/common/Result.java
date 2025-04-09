package com.happy_hao.rums.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class Result {
    public static final String STATUS_SUCCESS = "200";
    public static final String STATUS_SYS_ERROR = "500";

    private String status;// 业务状态码
    private String message;// 提示信息
    private Map<String, Object> data = new HashMap<>();// 响应数据

    private Result() {}

    public static Result success() {
        Result result = new Result();
        result.setStatus(STATUS_SUCCESS);
        result.setMessage("请求成功");
        return result;
    }

    public static Result error() {
        Result result = new Result();
        result.setStatus(STATUS_SYS_ERROR);
        result.setMessage("请求失败");
        return result;
    }

    public Result status(String status) {
        this.setStatus(status);
        return this;
    }

    public Result message(String message) {
        this.setMessage(message);
        return this;
    }

    public Result data(String key, Object value) {
        this.data.put(key, value);
        return this;
    }

    public Result data(Map<String, Object> data) {
        this.setData(data);
        return this;
    }
}