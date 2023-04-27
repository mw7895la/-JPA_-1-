package jpabook.jpabook.domain;

import jpabook.jpabook.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class Category {

    @Id
    @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    /** 카테고리도 리스트로 아이템을 가지고 아이템도 리스트로 카테고리를 가진다. 실무에서 ManyToMany 쓰지 말자*/
    @ManyToMany
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id") )      //중간 테이블 매핑을 해줘야 한다.  객체는 컬렉션이 각각 있어서 다대다가 가능한데, DB는 중간에 1:N N:1 관계로 풀어낼 테이블이 필요하다.
    private List<Item> items = new ArrayList<>();
    //inverseJoinColumns 는 category_item 테이블에 item쪽으로 들어가는 컬럼을 매핑해준다.

    /** 카테고리는 depth가 있기 때문에 하위 메뉴까지 가져올 수 있도록 구현해야 한다.  셀프 조인*/
    //카테고리 구조를 표현해보자.
    //같은 엔티티에 대해서 셀프로 양방향 매핑을 한 것.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")         //대상 테이블의 PK 즉 category_id
    private Category parent;        //Category 클래스 내부에 Category 클래스를 하나의 필드값으로 넣고, @ManyToOne 관계로 잡아준다. 자신의 PK를 부모로 삼는 parent 외래키를 넣어줬다
    //Category 1개 레코드의 parent값은 "육류" 가 되어 parent_id 값으로 저장되고
    // Category parent = new Category();
    // parent.setName("육류");    em.persist(parent);
    // Category child1 = new Category(); Category child2 = new Category();
    // child1.setName("돼지"); child2.setName("소");  .. child1.setParent(parent); child2.setParent(parent);
    // 이 parent가 List로 된 '돼지,소,닭' 등을 가지게 된다.
    //https://www.inflearn.com/course/lecture?courseSlug=%EC%8A%A4%ED%94%84%EB%A7%81%EB%B6%80%ED%8A%B8-JPA-%ED%99%9C%EC%9A%A9-1&unitId=24768&tab=community&category=questionDetail&q=369705

    //자식은 카테고리를 여러개를 가질 수 있다.
    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();       //하위 메뉴들을 가져오기 위해 List<Category> 형식을 갖는 child를 OneToMany로 설정.

    //==연관관계 편의 메서드==//
    //category 셀프로 양방향 매핑에 대한 편의 메서드
    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(this);
    }
}
