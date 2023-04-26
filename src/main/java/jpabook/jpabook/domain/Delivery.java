package jpabook.jpabook.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery")
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)        //ORDINAL 은 인덱스 처럼 사용이라 나중에 enum에 데이터 추가되면 수정해야되서 STRING으로 하자.
    private DeliveryStatus status;      //READY 준비, COMP 배송
}
