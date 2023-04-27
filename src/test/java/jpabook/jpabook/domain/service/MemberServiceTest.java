package jpabook.jpabook.domain.service;

import jpabook.jpabook.domain.Member;
import jpabook.jpabook.repository.MemberRepository;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

/** Junit 실행할 때 스프링이랑 같이 엮어서 실행 할래라고 하면 이걸 넣어주면 됨. @RunWith(SpringRunner.class)
 *  @SpringBootTest은 스프링 부트 띄우고 테스트할 거다. 이거 없으면 @Autowired 다 실패함.
 *  */

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class MemberServiceTest {

    //테스트는 2가지

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    EntityManager em;

    @Test
    //@Rollback(false)
    public void 회원가입() {
        //JPA에서 같은 트랜잭션 안에서 같은 엔티티 PK 값이 똑같으면 같은 영속성 컨텍스트에서 똑같은애가 관리가 된다.
        //given - 이런게 주어진다.
        Member member = new Member();
        member.setName("Kim");

        //when - 이렇게 하면
        Long savedId = memberService.join(member);
        //이거 루트 타고 들어가면 em.persist(member)를 하는데 트랜잭션 커밋 전까지는 인서트 쿼리가 나가지 않는다.
        //근데, Test Code에서는 예외적으로 롤백이 되기 때문에 인서트 쿼리가 나가지 않았던 것이다. 영속성 컨텍스트가 flush를 안한다.

        //then - 이렇게 된다는 것을 검증해라.
        //위에서 만든 멤버를 join한 후, 반환받은 pk id값으로 findOne 했을 때 둘이 같냐를 확인.
        //em.flush();     //영속성 컨텍스트에 있는 등록된 내용을 DB에 반영한다. 이러면 쿼리 볼 수 있음. 그리고 트랜잭션이니 Insert 되었던게 롤백된다.
        assertEquals(member, memberRepository.findOne(savedId));
    }

    @Test(expected = IllegalStateException.class)       //이렇게 예상되는 것이 IllegalStateException이면 테스트는 성공한다.
    public void 중복_회원_예외() {
        //중복 회원 검증 로직
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        /*memberService.join(member1);
        try {
            memberService.join(member2);    //여기서 예외가 발생해야 한다
        }catch(IllegalStateException e){
            return;
        }*/
        memberService.join(member1);
        memberService.join(member2);
        //과정 -> 처음에 member1 join시 검증로직에서 jpql쿼리를 날리니 flush되면서 select문 보임. -> 그 다음 em.persist(member1)이 일어남. -> member2를 join할 때 다시 검증로직이 호출됨
        // -> jpql 쿼리를 날리면서 flush가 되니 member1에 대한 인서트 쿼리가 날라가고 select문이 날라감. member2에 대한 insert 쿼리는 안날아감(마지막에 rollback 되니까.)

        //then
        Assert.fail("test code에서 예외가 발생해야 한다. ");    // Assert.fail은 뭐냐 코드가 돌다가 여기로 오면 안된다는 것. 즉, 테스트가 뭔가 잘못 되었다는 것.
    }

    /** 테스트를 외부 DB를 사용해서 하기가 좀 그래, in-Memory DB를 사용하자.
     *  1. src/main , src/test  로 폴더가 나뉨.
     *  2. test/resources 디렉토리를 만들어줌.
     *  3. 기본적으로 운영 로직은 src/main/resources  가 우선권을 가지고, 테스트는 src/test/resources 가 우선권을 가진다. 그래서 application.yml을 test에 만들자.
     *  4. gradle 라이브러리에 h2database가 들어가 있다. 이게 있으면, JVM 안에서 띄울 수가 있다. URL에다 메모리 모드로 띄우라고 하면 된다. yml의 url: jdbc:h2:mem:test로 바꿔주자.
     *  5. 근데 스프링 부트에선 놀라운 기능이 있다. 스프링 부트가 기본적으로 별도의 설정이 없으면 Memory 모드로 돌려버린다.
     * */


}