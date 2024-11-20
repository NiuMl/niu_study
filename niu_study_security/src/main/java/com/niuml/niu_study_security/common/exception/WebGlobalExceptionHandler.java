package com.niuml.niu_study_security.common.exception;

import com.niuml.niu_study_security.common.model.Result;
import com.niuml.niu_study_security.common.model.ResultBuilder;
import com.niuml.niu_study_security.common.utils.I18nMessageTool;
import com.niuml.niu_study_security.common.utils.JSON;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.HashMap;
import java.util.List;

@RestControllerAdvice
public class WebGlobalExceptionHandler {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(HttpServletResponse response, Exception e) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
        logger.info("服务器异常", e);
        return Result.fail("服务器异常");
    }

    @ExceptionHandler(value = NoResourceFoundException.class)
    public Result exceptionHandler(HttpServletResponse response, NoResourceFoundException e) {
        response.setStatus(HttpStatus.NOT_FOUND.value());
        return ResultBuilder.aResult()
                .msg("api not found")
                .code("api.not.found")
                .build();
    }

    @ExceptionHandler(value = AccessDeniedException.class)
    public Result accessDeniedExceptionHandler(HttpServletResponse response, AccessDeniedException e) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResultBuilder.aResult()
                .msg("全局拦截：无权访问")
                .code("low.power")
                .build();
    }
    @ExceptionHandler(value = AuthenticationCredentialsNotFoundException.class)
    public Result authenticationCredentialsNotFoundExceptionHandler(HttpServletResponse response, AuthenticationCredentialsNotFoundException e) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResultBuilder.aResult()
                .msg("全局拦截：无权访问2")
                .code("low.power")
                .build();
    }
    @ExceptionHandler(value = ExpiredJwtException.class)
    public Result expiredJwtExceptionHandler(HttpServletResponse response, ExpiredJwtException e) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        return ResultBuilder.aResult()
                .msg("${token.expired:jwt过期")
                .code("token.expired")
                .build();
    }

    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    public Result exceptionHandler(HttpServletResponse response, MethodArgumentNotValidException e) {
        response.setStatus(HttpStatus.BAD_REQUEST.value());

        // 国际化翻译 数据校验异常信息
        BindingResult result = e.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();
        HashMap<String, String> errorFields = new HashMap<>();
        for (FieldError error : fieldErrors) {
            String fieldName = error.getField();
            errorFields.put(fieldName, I18nMessageTool.translate(error.getDefaultMessage()));
        }

        return Result.fail(JSON.stringify(errorFields));
    }

    @ExceptionHandler(value = BaseException.class)
    public Result exceptionHandler(HttpServletResponse response, BaseException e) {
        response.setStatus(e.getHttpStatus().value());
        return createResult(e);
    }

    private Result createResult(BaseException e) {
        return ResultBuilder.aResult()
                .msg(e.getMessage())
                .code(e.getCode() == null ? Result.FAIL_CODE : e.getCode())
                .build();
    }
}
