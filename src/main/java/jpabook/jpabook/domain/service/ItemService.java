package jpabook.jpabook.domain.service;

import jpabook.jpabook.domain.item.Book;
import jpabook.jpabook.domain.item.Item;
import jpabook.jpabook.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)     //클래스 레벨에 붙이면 전체, 메서드 레벨에 붙인건 해당 메서드만, 구체적인게 우선권.
@RequiredArgsConstructor
public class ItemService {

    /** 서비스는 단순히 리포지토리에 위임만 하는 기능이라 개발은 쉽다.
     * 영한님은 컨트롤러에서 바로 리포지토리 접근하는 방법도 괜찮다고 함. */


    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);      /** 단순 저장 말고 업데이트를 위한 book 엔티티가 넘어온다 */
    }

//    /** 변경 감지 기능 이용( * Update )*/
//    @Transactional
//    public void updateItem(Long itemId, Book param) {
//        Item findItem = itemRepository.findOne(itemId);
//        // itemId를 기반으로 실제 DB에 있는 데이터를 찾아서 영속상태로 JPA가 관리해준다.
//        findItem.setPrice(param.getPrice());
//        findItem.setName(param.getName());
//        findItem.setStockQuantity(param.getStockQuantity());
//
//        //itemRepository.save(findItem);  이렇게 호출할 필요없다.!  이미 변경감지가 일어나서 flush때 바뀐 값들을 확인후 쿼리저장소에 업데이트문 쌓고 최종 커밋때 날린다!
//    }


    /** 병합이 아닌 변경감지를 이용하는 방법 1 - 파라미터가 별로 없을 때*/
//    @Transactional
//    public void updateItem(Long itemId, String name, int price, int stockQuantity) {
//        Item findItem = itemRepository.findOne(itemId);
//        findItem.setName(name);
//        findItem.setPrice(price);
//        findItem.setStockQuantity(stockQuantity);
//    }

    /** 병합이 아닌 변경감지를 이용하는 방법 1 - 파라미터가 많을 때 - DTO이용*/
    @Transactional
    public void updateItem(Long itemId,UpdateItemDto updateItemDto) {
        Item findItem = itemRepository.findOne(itemId);
        findItem.changeItem(updateItemDto);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
