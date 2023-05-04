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
            //Item merge = em.merge(item);
            em.merge(item);
            //준영속 상태의 엔티티를 다시 영속 상태로 변경한다.
            //업데이트와 비슷하다. 상품의 경우는 id값을 부여받은 뒤에 수정이 이뤄진다.
            /** 넘어온 item의 식별자 값으로 1차 캐시에서 찾아보는데 없다? 그럼 DB에서 가져와서 1차캐시에 저장.
             *  찾아온 다음, 파라미터로 넘어온 item의 데이터들로 바꿔치기 한다. 그다음 바뀐 데이터를 반환해준다.
             *  트랜잭션 커밋 시점에 변경 감지 기능이 동작해서 데이터베이스에 UPDATE SQL이 실행
             *  Item merge = em.merge(item);        --->> 반환된 merge가 영속성 컨텍스트에서 이제 관리되는 객체고  item은 관리하지않는다.
             *  대신 문제점이 있다. 병합은 값을 전부다 바꾸기 때문에,  넘어오지 않는 값은 null로 채워진다..  **그래서 되도록이면 변경감지(dirty checking)를 사용하자.
             *
             *  아래와 동일하다.
             *     @Transactional
             *     public Item updateItem(Long itemId, Book param) {
             *         Item findItem = itemRepository.findOne(itemId);
             *         // itemId를 기반으로 실제 DB에 있는 데이터를 찾아서 영속상태로 JPA가 관리해준다.
             *         findItem.setPrice(param.getPrice());
             *         findItem.setName(param.getName());
             *         findItem.setStockQuantity(param.getStockQuantity());
             *         return findItem;
             *     }
             * */
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
