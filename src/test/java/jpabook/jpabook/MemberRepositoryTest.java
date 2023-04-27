package jpabook.jpabook;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;


//@RunWith(SpringRunner.class)        //SpringRunner.class 에서는 결국 어플리케이션 내에 @SpringBootApplication이 지정된 클래스를 실행하게 되고 우리는 테스트시 스프링 컨테이너를 활용할 수 있게 된다
//@SpringBootTest
public class MemberRepositoryTest {

//    @Autowired
//    MemberRepository_test memberRepository;
//
//    //No EntityManager with actual transaction available for current thread  트랜잭션이 없다. 엔티티 매니저는 항상 트랜잭션을 통해서 사용되어야 한다!!
//    @Test
//    @Transactional          //테스트 코드에서는 예외적으로 성공해도 롤백까지 된다
//    @Rollback(false)
//    public void testMember() {
//        //given
//        Member1 member = new Member1();
//        member.setUsername("memberA");
//
//        //when
//        Long savedId = memberRepository.save(member);
//
//        //then
//        Member1 findMember = memberRepository.find(savedId);
//        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
//        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
//        Assertions.assertThat(findMember).isEqualTo(member);        //저장한것과 조회한게 같을까? 같다.   같은 영속성 컨텍스트에서는 id값이 같으면 같은 엔티티로 인식. 1차 캐시에 있으니까 select 자체도 안한것.
//        System.out.println("(findMember==member = " + (findMember == member));
//    }


}