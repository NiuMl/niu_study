package com.niuml.niu_study_spring_web_check.advices;

import com.niuml.niu_study_spring_web_check.common.CheckException;
import com.niuml.niu_study_spring_web_check.common.Res;
import com.niuml.niu_study_spring_web_check.common.SelfException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.stream.Collectors;

import static com.niuml.niu_study_spring_web_check.enums.ExceptionEnum.JSON_ERROR;

/***
 * @author niumengliang
 * Date:2023/8/3
 * Time:15:14
 */
@Log4j2
@ControllerAdvice
public class SelfExceptionHandler {


    /**
     * 所以异常的祖宗，当有的异常是下面的异常无法
     * 捕获的时候，就会执行这个方法
     */
    @ExceptionHandler(value =Exception.class)
    @ResponseBody
    public Res exceptionHandler(Exception e){
        log.info("全局异常捕获>>>:"+e);
        return Res.fail(e.getMessage());
    }

    /**
     * 自定义异常，可以直接在程序里面抛出
     */
    @ExceptionHandler(value =SelfException.class)
    @ResponseBody
    public Res selfExceptionHandler(SelfException e){
        log.info("自定义异常捕获>>>:"+e);
        return Res.fail(e);
    }

    /**
     *  好像没啥卵用
     */
    @ExceptionHandler(value = CheckException.class)
    @ResponseBody
    public Res checkExceptionHandler(CheckException e){
        log.info("自定义验证异常捕获>>>:"+e);
        return Res.fail(e);
    }

    /**
     * 拦截输入的json格式不正确情况
     */
    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    @ResponseBody
    public Res httpMessageNotReadableExceptionHandler(HttpMessageNotReadableException e){
        log.info("自定义json验证异常捕获>>>:"+e);
        return Res.fail(JSON_ERROR);
    }

    /**
     * 自动表单验证
     */
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseBody
    public Res httpMessageNotReadableExceptionHandler(MethodArgumentNotValidException e){
        log.info("自定义自动验证输入项异常捕获>>>:"+e);
        String result = e.getBindingResult().getAllErrors().stream().map(a -> a.getDefaultMessage()).collect(Collectors.joining(","));
        return Res.fail(result);
    }
}
