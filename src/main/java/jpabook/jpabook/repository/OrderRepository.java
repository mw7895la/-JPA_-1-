package jpabook.jpabook.repository;

import jpabook.jpabook.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class, id);
    }

    //주문 내역을 보면 검색이 된다.     검색은 회원이름과 주문상태로.
    public List<Order> findAllByString(OrderSearch orderSearch) {
//        em.createQuery("select o from Order as o join o.member as m" + " where o.status=:status" +
//                        " and m.name like :name", Order.class).setParameter("status", orderSearch.getOrderStatus()).setParameter("name", orderSearch.getMemberName())
//                .setMaxResults(1000).getResultList();       //최대 1000건
        //지금 위에거는 회원이름과 주문상태를 다 입력해줬을 경우의 findAll이다.  회원이름 , 주문상태 둘다 들어온 데이터가 없거나 둘중 하나면 들어오면?? 동적쿼리로 바꿔야한다.

        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;
        //주문 상태 검색
        if (orderSearch.getOrderStatus() != null) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " o.status = :status";
        }

        //회원 이름 검색
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            if (isFirstCondition) {
                jpql += " where";
                isFirstCondition = false;
            } else {
                jpql += " and";
            }
            jpql += " m.name like :name";
        }
        //작성한 JPQL을 실행시키기 위해 만드는 쿼리 객체이다.
        TypedQuery<Order> query = em.createQuery(jpql, Order.class)
                .setMaxResults(1000);

        if (orderSearch.getOrderStatus() != null) {
            query = query.setParameter("status", orderSearch.getOrderStatus());
        }
        if (StringUtils.hasText(orderSearch.getMemberName())) {
            query = query.setParameter("name", orderSearch.getMemberName());
        }
        return query.getResultList();
    }

    /** JPA Criteria 이거 기본편에서도 소개 안했음. */
    //jpql을 자바 코드로 작성할 수 있도록 해줌.
//    public List<Order> findAllByCriteria(OrderSearch orderSearch) {
//
//    }
}
