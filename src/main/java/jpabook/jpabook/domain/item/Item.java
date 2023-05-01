package jpabook.jpabook.domain.item;

import jpabook.jpabook.domain.*;
import jpabook.jpabook.domain.exception.NotEnoughStockException;
import lombok.Getter;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype")
public abstract class Item {
    //상속 관계 매핑을 해야된다.
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

    //==비즈니스 로직==//
    //재고를 늘리고 줄이는 로직//
    //엔티티 자체가 해결할 수 있는것들은 엔티티 안에 비즈니스 로직을 넣는게 좋다.//
    //보통 서비스 로직에서 get ,set 이용해서 값을 바꿨을 것.  하지만 데이터를 가지고 있는 곳에서 비즈니스 로직을 짜는게 응집력이 있다.
    //우리는 Setter를 없애고 직접 Item 엔티티 안에서 구현

    /** stock 증가 */
    public void addStock(int quantity) {
        this.stockQuantity +=quantity;
    }

    /** stock 감소 */
    public void removeStock(int quantity) {
        //0보다 줄어들면 안되니까 체크 로직도 있어야 함.
        int restStock = this.stockQuantity - quantity;
        if(restStock <0){
            throw new NotEnoughStockException("need more stock");
        }
        this.stockQuantity = restStock;
    }
}
