package jpabook.jpabook.repository;

import jpabook.jpabook.domain.item.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    //상품 저장
    public void save(Item item) {
        if (item.getId() == null) {
            //아이템을 처음 저장할 때는 pk인 id가 없다.그래서 아예 신규로 등록하는 로직
            em.persist(item);
        }else{
            em.merge(item);
            //준영속 상태의 엔티티를 다시 영속 상태로 변경한다.
            //업데이트와 비슷하다. 상품의 경우는 id값을 부여받은 뒤에 수정이 이뤄진다.
        }
    }

    public Item findOne(Long id){
        return em.find(Item.class, id);
    }

    public List<Item> findAll(){
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }


}
