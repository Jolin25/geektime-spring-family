package geektime.spring.springbucks.waiter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.util.HashMap;
import java.util.Map;
// 说明自己是全局异常处理，表明处理哪种类型的异常，如何处理异常
/**
 * knowledge point:
 * ControllerAdvice + ResponseBody
 * 所以map会被作为ResponseBody返回
 */
@RestControllerAdvice
public class GlobalControllerAdvice {
    // 说明这个ExceptionHandler用于处理ValidationException
    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> validationExceptionHandler(ValidationException exception) {
        Map<String, String> map = new HashMap<>();
        map.put("message", exception.getMessage());
        return map;
    }
}
