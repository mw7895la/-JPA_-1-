package jpabook.jpabook.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.sql.Array;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;      //연관관계 주인.

    //jpql select o from order o; 이렇게 해서 가져오면 이게 SQL로 그대로 번역이 된다. (EAGER,LAZY 이런거 무시) 그래서 100개 가져왔더니 member가 EAGER로 되어있다. 그래서 또 member를 조회하기 위해 쿼리가 날라간다.

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)       //cascadeType ALL  - orderitem 객체 3개 만들어서 Order의 orderitems 리스트에 넣고 Order만 persist 해주면 연관된 것들 다 영속화 된다. 각각 엔티티를 persist 하는걸 줄여주는 것.
    private List<OrderItem> orderItems = new ArrayList<>();

    //persist(orderitemA)
    //persist(orderitemB)
    //persist(orderitemC)

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    //자바 8이상에서는 이렇게 그냥 LocalDateTime을 쓰면 하이버네이트가 알아서 지원해준다. 원래는 @Temporal 어노테이션을 사용했어야 했다.
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;     //주문상태  [ORDER, CANCEL]


    //==연관관계 메서드==//
    public void setMember(Member member) {
        this.member = member;           //this는 order
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    /*public static void main(String[] args) {
        Member member = new Member();
        Order order = new Order();
        member.getOrders().add(order);
        order.setMember(member);       //를 위의 setMember()로 표현

    }*/


}
