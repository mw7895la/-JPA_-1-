package jpabook.jpabook.domain;

import jpabook.jpabook.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)  /** JPA에선 스팩상 protected까지 기본생성자를 만들 수 있게 허용해준다.(인식 하겠다는 것) */
public class OrderItem {

    @Id @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")       //OrderItem 에서 Item으로 화살표가 있다. 단방향 관계  Item입장에서 내가 어느 주문상품에 들어있는지 찾을 필요가 없다.
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;     //주문 가격
    private int count;          //주문 수량




    //==생성 메서드==//
    public static OrderItem createOrderItem(Item item,int orderPrice,int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);
        //넘어온 것 만큼 재고를 줄여줘야 한다.
        item.removeStock(count);
        return orderItem;
    }

    //==비즈니스 로직==//
    public void cancel(){
        getItem().addStock(count);  //item.addStock(count);
        //아이템에 재고를 다시 주문수량만큼 늘려줘야 한다.
        //Order 클래스의 cancel 비즈니스 로직에서 루프의 orderItem.cancel();  이곳의 cancel() 메서드가 호출되고, orderItem이 갖고있던 count만큼 재고에 다시 + 된다.

    }

    public int getTotalPrice() {
        return getOrderPrice() * getCount();
        //return orderPrice * count; 해도 아무 문제 없다. getter는 영한님이 그냥 아무 의미 없이 한 것이지만 설명을 참고하자. https://www.inflearn.com/questions/20180/orderitem-%EA%B4%80%EB%A0%A8
    }
}
