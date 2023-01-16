package geektime.spring.springbucks.waiter.controller.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ResponseStatus;
/** knowledge point: 这是一个自定义异常类
 *  1. ResponseStatus 注解 用于指定异常出现后返回给前端的 Http 状态
 * */
@ResponseStatus(HttpStatus.BAD_REQUEST)
@Getter
@AllArgsConstructor
public class FormValidationException extends RuntimeException {
    private BindingResult result;
}
