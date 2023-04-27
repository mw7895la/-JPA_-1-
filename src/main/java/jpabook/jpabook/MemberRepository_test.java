package jpabook.jpabook;

import javax.persistence.EntityManager;

//@Repository
public class MemberRepository_test {

    /** JPA를 쓰기 때문에 이제 내가 알던 기존과 다름.*/

    //일반 적으로 스프링은 싱글톤 기반으로 동작해서, 빈은 모든 쓰레드가 공유한다.
    //하지만 @PersistenceContext로 주입받는 EntityManager를 스프링 컨테이너가 초기화 되면서 Proxy로 감싸고 EntityManager를 호출 시 마다 Proxy를 통해 EntityManager를 생성하여 Thread-safe를 보장한다.


    //스프링 컨테이너에서 관리되는 빈이 아닌, application-managed EntityManager bean을 Thread 하나 당 주입하기 위해 사용하는 어노테이션이다 (스프링이 빌려쓰는 것.)
    //JPA를 쓰기 때문에 EntityManager가 있어야 한다. 스프링 부트가 저 어노테이션이 있으면  엔티티매니저를 주입해준다. 엔티티 매니저 팩토리 생성하는것은 스프링 부트가 다  해준다.
    //@PersistenceContext //영속성 컨텍스트
    private EntityManager em;

    public Long save(Member1 member) {
        em.persist(member);
        return member.getId();      //왜 member를 반환안하고 getId()를 반환하지? , 저장을 하고 나면 가급적 side-effect를 일으키지 않기 위해 id 정도만 반환한다.
    }

    public Member1 find(Long id) {
        return em.find(Member1.class, id);       //PK
    }
}
