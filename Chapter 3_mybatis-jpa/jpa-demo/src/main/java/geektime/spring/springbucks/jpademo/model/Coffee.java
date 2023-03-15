package geektime.spring.springbucks.jpademo.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.UpdateTimestamp;
import org.joda.money.Money;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

@Entity // 说明这是一个jpa实体类
@Table(name = "T_MENU") // 说明 Coffee 这个 pojo 对应的是 T_MENU 这张表
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coffee implements Serializable {
    @Id // 表明这个属性对应的字段是一个主键
    @GeneratedValue // 表明主键自增
    private Long id;
    private String name;
    @Column // 表明这个属性对应一个字段，没有指明名字的话，就是属性名
    // Type：指明使用 type 来根据映射关系进行转换，parameters 可以用来指定某个 字段的值
    @Type(type = "org.jadira.usertype.moneyandcurrency.joda.PersistentMoneyAmount",
            parameters = {@org.hibernate.annotations.Parameter(name = "currencyCode", value = "CNY")})
    private Money price;
    // 可以指明不然修改
    @Column(updatable = false)
    // 就是特地给 createtime 来用的
    @CreationTimestamp
    private Date createTime;
    // 就是特地给 updatetime 来用的
    @UpdateTimestamp
    private Date updateTime;
}
