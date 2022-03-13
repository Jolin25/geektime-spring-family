package geektime.spring.springbucks.waiter.support;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.joda.money.CurrencyUnit;
import org.joda.money.Money;
import org.springframework.boot.jackson.JsonComponent;

import java.io.IOException;

/**
 * JsonComponent注解用于将Jackson的序列化和反序列化器注册到对应的类上
 *
 * @author Joly
 */
@JsonComponent
public class MoneyDeserializer extends StdDeserializer<Money> {
    protected MoneyDeserializer() {
        super(Money.class);
    }

    /**
     * 将Json对象转为Java对象
     *
     * @param
     * @return
     * @date 2022/3/13
     */
    @Override
    public Money deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        return Money.of(CurrencyUnit.of("CNY"), p.getDecimalValue());
    }
}
