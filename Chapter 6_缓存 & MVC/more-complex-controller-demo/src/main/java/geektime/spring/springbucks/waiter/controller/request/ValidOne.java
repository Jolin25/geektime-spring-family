package geektime.spring.springbucks.waiter.controller.request;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * @author jrl
 * @date Create in 21:17 2022/3/11
 */

public class ValidOne {
    @NotEmpty
    private String name;
    @NotNull
    private String age;

    public ValidOne(@NotEmpty String name, @NotNull String age) {
        this.name = name;
        this.age = age;
    }

    public ValidOne() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }
}
