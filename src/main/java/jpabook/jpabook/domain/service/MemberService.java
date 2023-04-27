package jpabook.jpabook.domain.service;

import jpabook.jpabook.MemberRepository_test;
import jpabook.jpabook.domain.Member;
import jpabook.jpabook.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
//스프링이 제공하는 트랜잭션 어노테이션을 사용하는게 좋다. 쓸 수 있는 옵션이 많아서.
//읽기 전용 트랜잭션이라고 하면, 단순히 조회만 하도록 해서 리소스 낭비를 방지한다.
public class MemberService {

    private final MemberRepository memberRepository;
    //필드 주입의 단점. 다른것으로 바꿔 끼질 못함. setter보단 궁극적으로 생성자 주입을 사용하자. final 필드만 가지고있는 필드에 관한 생성자 @RequiredArgsContructor

    @Autowired
    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /** JPA의 모든 데이터 변경이나 로직들은 Transaction 안에서 다 실행 되어야 한다. */

    /** 회원 가입
     * 구체적인것이 더 우선권이 있어서 서비스 전체에 readOnly=true를 해줘도 join에서는 기본 default인 readOnly=false가 적용된다.  여기 true면 데이터 변경 안된다.*/
    @Transactional
    public Long join(Member member) {
        //중복 회원 검증
        validateDuplicateMember(member);
        memberRepository.save(member);      //em.persist 할때 PK의 값은 DB에 들어갈 시점이 아니어도 채워주는게 보장이 된다
        return member.getId();              //id라도 돌려줘야 뭐가 저장됐는지 알 수 있다.
    }

    /**회원 전체 조회 */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /** 단건 조회 */
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    /** 동시에 똑같은 이름이 가입을 요청하면 둘다 정상 로직을 타기 때문에 가입이 된다. 그래서 최후로 DB에서 유니크 제약조건을 걸자. */
    private void validateDuplicateMember(Member member) {
        //Exception
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }


}
