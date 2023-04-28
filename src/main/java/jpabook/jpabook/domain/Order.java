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

    //==주문 생성 메서드==//
    //Order는 orderitem도 있어야되고 delivery 연관관계도 있기 때문에 복잡한 생성은 별도의 메서드가 있으면 좋다.
    /** JPA 기본편 실습시 항상 해줬던 Member와 Team의 관계를 생각.
     *  Team을 먼저 생성 후 Member에 Team을 Set해주는 과정을 아래처럼 편의 관계 메서드 구현.
     *  this.member = member   -> Member.setTeam(team)
     *  member.getOrders().add(this) -> team.getMembers().add(member)*/
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems) {    //가변인자 타입은 OrderItem 0~N개 넘어오기 때문에 배열로 처리된다.
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);

        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);     //주문 상태
        order.setOrderDate(LocalDateTime.now());
        return order;
        //이렇게 하면 order가 연관관계를 쫙 걸면서 세팅이 되고 상태와 주문시간까지 세팅이 된다.
    }

    //==비즈니스 로직==//
    /** 주문 취소 -재고 다시 올려줘야 함.  - 비즈니스 체크 로직이 엔티티 안에 있다.*/
    public void cancel(){
        //이미 배송이 완료되어버렸다
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }
        this.setStatus(OrderStatus.CANCEL);

        //루프를 돌면서 재고를 원복 ( 보라색 글씨는 해당클래스 내부에있어서 this.orderItems 와 같다.)
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();     //한번 주문할 때 고객이 상품 2개 주문하면 orderitem 2개 각각 cancel 날려줘야 함.
        }
    }

    //==조회 로직==//
    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice(){
        //내 주문의 orderitem들을 다 더하면 된다.
        int totalPrice=0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
            //orderItem에 있는 전체 가격을 가져온다. 왜냐? 주문했을 때 주문 가격과 수량이 있기 때문에 둘다 곱해줘야 한다. 이건 orderItem에서 구현해주자.
        }
        return totalPrice;

        //return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();    이렇게 표현할 수 있다 *람다
    }


}
