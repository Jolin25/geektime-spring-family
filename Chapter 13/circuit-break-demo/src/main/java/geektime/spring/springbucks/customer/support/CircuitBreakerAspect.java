package geektime.spring.springbucks.customer.support;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * knowledge point:  利用AOP来做一个拦截，实现断路模式
 */
@Aspect
@Component
@Slf4j
public class CircuitBreakerAspect {
    // 阈值
    private static final Integer THRESHOLD = 3;

    // 调用失败的次数
    private Map<String, AtomicInteger> counter = new ConcurrentHashMap<>();
    // 断路保护的次数
    private Map<String, AtomicInteger> breakCounter = new HashMap<>();
    private Map<String, Integer> breakCounter2 = new ConcurrentHashMap<>();

    // DONE_Joly:是哪里在控制执行重新请求--->手动执行的多次请求...
    @Around("execution(* geektime.spring.springbucks.customer.integration..*(..))")
    public Object doWithCircuitBreaker(ProceedingJoinPoint pjp) throws Throwable {
        // DONE_Joly: signature ---> 被调的方法信息（包，方法签名）
        String signature = pjp.getSignature().toLongString();
        log.info("Invoke {}", signature);
        Object retVal;
        try {
            /** knowledge point:  其实这就是个概率的思想（事先假设大概率会成功或失败）。
             * 一开始假设会成功，失败的次数多了以后，
             * 就假设会失败，偶尔尝试看看会不会成功.
             * 如果成功了，就重新进入认为成功概率高的假设。*/
            /*如果失败次数高于阈值，就开始熔断。如果熔断次数达到阈值，就尝试一下是否能调通。*/
            if (counter.containsKey(signature)) {
                if (counter.get(signature).get() > THRESHOLD &&
                        breakCounter.get(signature).get() < THRESHOLD) {
                    log.warn("Circuit breaker return null, break {} times.",
                            breakCounter.get(signature).incrementAndGet());
                    return null;
                }
            } else {
                counter.put(signature, new AtomicInteger(0));
                breakCounter.put(signature, new AtomicInteger(0));
            }
            retVal = pjp.proceed();
            /*如果成功，就把失败次数和熔断次数都设置为0*/
            counter.get(signature).set(0);
            breakCounter.get(signature).set(0);
        } catch (Throwable t) {
            /*如果调用失败，就失败次数+1*/
            log.warn("Circuit breaker counter: {}, Throwable {}",
                    counter.get(signature).incrementAndGet(), t.getMessage());
            /*在已经启用断路的情况下，熔断次数每到设定的阈值，就会去探火一次，看看会不会能调通了。
             * 如果又失败了，就把熔断次数置为零，重新统计次数，准备下次探火*/
            breakCounter.get(signature).set(0);
            throw t;
        }
        return retVal;
    }
}
