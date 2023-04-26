package jpabook.jpabook.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id")     //따로 컬럼 안정해주면 id로 된다.
    private Long id;

    private String name;

    @Embedded
    private Address address;

    //mappedBy -> Orders 테이블에 있는 member 필드에 의해서 나는 매핑 된 것이다.
    @OneToMany(mappedBy = "member")     //Member는 orders를 리스트로 가지고있다. Order도 Member를 가지고 있다. 양방향 참조. DB의 외래키는 Orders에 member_id 하나밖에 없다.
    private List<Order> orders = new ArrayList<>();
}
