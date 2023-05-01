package jpabook.jpabook.repository;

import jpabook.jpabook.domain.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearch {
    private String memberName;              //회원 이름
    private OrderStatus orderStatus;        //주문의 상태 [ORDER, CANCEL]
}
