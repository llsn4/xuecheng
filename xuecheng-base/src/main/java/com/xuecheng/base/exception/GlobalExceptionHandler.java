package com.xuecheng.base.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 苏航
 * @description 全局异常处理器
 * @date 2023/4/6 17:11
 **/

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 捕获系统自定义异常
     * @param e
     * @return
     */
    @ExceptionHandler(XueChengException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorMessage customException(XueChengException e){
        log.error("系统异常,信息:{}",e.getErrMessage());
        //解析异常信息
        String errMessage = e.getErrMessage();
        return new RestErrorMessage(errMessage);

    }

    /**
     * 捕获其他异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorMessage exception(Exception e){
        log.error("系统异常,信息:{}",e.getMessage());
        //解析异常信息
        String errMessage = e.getMessage();
        return new RestErrorMessage(CommonError.UNKOWN_ERROR.getErrMessage());

    }

    /**
     * 捕获JSR303异常
     * @param e
     * @return
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorMessage methodArgumentNotValidException(MethodArgumentNotValidException e){
        BindingResult bindingResult = e.getBindingResult();

         List<String> errorList=bindingResult.getFieldErrors().stream().map(
                 (DefaultMessageSourceResolvable::getDefaultMessage)).collect(Collectors.toList());
        String errMessage = StringUtils.join(errorList, ",");

        log.error("系统异常,信息:{}",errMessage);
        //解析异常信息
       // String errMessage = e.getMessage();
        return new RestErrorMessage(errMessage);

    }
}
