package geektime.spring.data.simplejdbcdemo;

import org.springframework.dao.DuplicateKeyException;

// 这里说明了自定义异常是代替了哪个已有的异常
public class CustomDuplicatedKeyException extends DuplicateKeyException {
    public CustomDuplicatedKeyException(String msg) {
        super(msg);
    }

    public CustomDuplicatedKeyException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
