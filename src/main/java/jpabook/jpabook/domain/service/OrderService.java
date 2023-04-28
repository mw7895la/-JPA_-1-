package jpabook.jpabook.domain.service;

import jpabook.jpabook.domain.Delivery;
import jpabook.jpabook.domain.Member;
import jpabook.jpabook.domain.Order;
import jpabook.jpabook.domain.OrderItem;
import jpabook.jpabook.domain.item.Item;
import jpabook.jpabook.repository.ItemRepository;
import jpabook.jpabook.repository.MemberRepository;
import jpabook.jpabook.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문 - 데이터 변경부분이니 트랜잭션 구체적선언, 그리고 member와 item에 대한 리포지토리도 필요함.
     * order 메서드를 다 작성하고 나서 보면, 원래 딜리버리도 리포지토리가 있어야 하고 거기서 save해서 넣어주고 orderItem도 jpa에 값을 넣어주고 Order.createOrder()에 세팅을 해줘야 한다.
     * 근데 아래처럼 order 하나만 저장해줘도 정상 동작한다. 왜 ??????????
     * 우리가 CascadeType.ALL로 했다 !!
     * order를 persist하면 orderItems 컬렉션안에 있는 orderItem도 persist 강제로 날려준다. delivery도 마찬가지 !
     *
     * 어디까지 Cascade.ALL로 해야되나..  어떤 개념에서 쓰면 좋냐면, Order같은 경우 order가 delivery를 관리하고, order가 orderitem도 관리한다. ◆◆이 정도 그림에서만 써야한다 - persist하는 라이프 사이클이 완전 똑같아서 가능했다. ◆◆
     * 그러니까, 참조하는 주인이 private 오너인 경우만 써야한다.  delivery는 지금 order말고 다른데 아무데도 안쓴다. orderitem도 마찬가지, orderitem도 order에서만 참조해서 쓴다.
     * orderitem이 다른것들을 참조하지만  다른 곳에서 orderitem을 참조하는 데는 order 밖에 없다.
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        //회원에 있는 address를 그대로 넣어주자. 실제로는 따로 작성해야겠지.
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        //OrderItem orderItem = new OrderItem();
        //orderItem.setCount()~ 이런식으로 할 수도 있지만 유지보수 하기가 나중에 힘들어진다.  이런 스타일의 생성을 막아야 한다. 생성자를 protected로 하자.

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장
        orderRepository.save(order);

        return order.getId();       //order의 식별자 값만 반환한다.
    }

    //취소
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소
        order.cancel();
        //이 코드는 JPA의 강점을 나타내준다. 평상시 SQL을 직접 다루는 라이브러리에선, setStatus를 변경한 다음 바깥에서(여기) 직접 변경 sql을 짜야하고, 재고도 +하는 sql도 직접 짜서 올려야 한다.
        //즉 SQL을 직접 짜면 서비스 계층에다 비즈니스 로직을 다 써야한다.
        //JPA로 하면 엔티티 안에 데이터만 바꾸면  Dirty Checking 변경감지가 일어나면서  변경된 내역들을 다 찾아내서 Update 쿼리가 날라간다.
    }

    //검색
//    public List<Order> findOrders(OrderSearch orderSearch) {
//
//    }
}
