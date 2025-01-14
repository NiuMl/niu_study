package com.niuml.niu_study_security_all_i18n.common.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashMap;

/**
 * 响应信息主体
 *
 */
@Data
public class Result implements Serializable {

  @Serial
  private static final long serialVersionUID = -6149948941889889657L;
  private int code;
  private String message;
  private Object data;

  public static final int SUCCESS_CODE = 200;
  public static final int FAIL_CODE = 999;

  public static Result success() {
    return ResultBuilder.initResult()
        .data(null)
        .code(SUCCESS_CODE)
        .msg(null)
        .build();
  }

  public static Result success(String message) {
    return ResultBuilder.initResult()
        .data(null)
        .code(SUCCESS_CODE)
        .msg(message)
        .build();
  }
  public static Result success(Object data,String message,int code) {
    return ResultBuilder.initResult()
        .data(data)
        .code(code)
        .msg(message)
        .build();
  }

  public static Result row(int row) {
    HashMap<String, Object> data = new HashMap<>();
    data.put("row", row);
    if (row > 0) {
      return data(data);
    } else {
      return fail(data);
    }
  }

  public static Result data(Object data) {
    return ResultBuilder.initResult()
        .data(data)
        .code(SUCCESS_CODE)
        .msg(null)
        .build();
  }

  public static Result data(Object data, String msg) {
    return ResultBuilder.initResult()
        .data(data)
        .code(SUCCESS_CODE)
        .msg(msg)
        .build();
  }

  public static Result fail(Object data, String msg) {
    return ResultBuilder.initResult()
        .data(data)
        .code(FAIL_CODE)
        .msg(msg)
        .build();
  }
  public static Result fail(Object data, String msg,int code) {
    return ResultBuilder.initResult()
        .data(data)
        .code(code)
        .msg(msg)
        .build();
  }

  public static Result fail(String msg) {
    return ResultBuilder.initResult()
        .data(null)
        .code(FAIL_CODE)
        .msg(msg)
        .build();
  }

  public static Result fail(Object data) {
    return ResultBuilder.initResult()
        .data(data)
        .code(FAIL_CODE)
        .msg(null)
        .build();
  }

  public Result() {
  }
}
