package jpabook.jpabook.domain;

import jpabook.jpabook.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
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
}
