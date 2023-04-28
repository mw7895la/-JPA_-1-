package jpabook.jpabook.domain.service;

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
        itemRepository.save(item);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
