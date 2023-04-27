package jpabook.jpabook.repository;

import jpabook.jpabook.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceUnit;
import java.util.List;

@Repository
public class MemberRepository {

    //스프링 DATA JPA 라이브러리를 사용하면 바로 아래와 같이 사용할 수 있다.
    /*private final EntityManager em;
    @Autowired
    public MemberRepository(EntityManager em) {
        this.em = em;
    }*/

    //JPA를 사용한다. 스프링이 엔티티 메니저를 만들어서 injection 해준다.
    @PersistenceContext
    private EntityManager em;

//    엔티티 매니저 팩토리 주입 방법. 쓸일은 거의 없다.
//    @PersistenceUnit
//    private EntityManagerFactory emf;

    public void save(Member member) {
        em.persist(member);

    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    //리스트 조회
    public List<Member> findAll(){
        //전부다 찾는 방법 jpql을 작성  , 테이블이 아닌 엔티티를 대상으로 쿼리
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name=:name", Member.class).setParameter("name", name).getResultList();
    }
}
