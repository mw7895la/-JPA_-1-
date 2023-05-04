package jpabook.jpabook.controller;

import jpabook.jpabook.domain.item.Book;
import jpabook.jpabook.domain.item.Item;
import jpabook.jpabook.domain.service.ItemService;
import jpabook.jpabook.domain.service.UpdateItemDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/items/new")
    public String createForm(Model model) {
        model.addAttribute("form", new BookForm());
        return "items/createItemForm";
    }

    @PostMapping("/items/new")
    public String create(BookForm form) {
        Book book = new Book();         //아래 Setter들은 스태틱 메서드로 집어 넣어주는게 좋다.
        book.setName(form.getName());
        book.setPrice(form.getPrice());
        book.setStockQuantity(form.getStockQuantity());
        book.setAuthor(form.getAuthor());
        book.setIsbn(form.getIsbn());

        itemService.saveItem(book);

        return "redirect:/items";
    }

    @GetMapping("/items")
    public String list(Model model) {
        List<Item> items = itemService.findItems();
        model.addAttribute("items", items);
        return "items/itemList";
    }

    @GetMapping("/items/{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId")Long itemId,Model model) {
        Book item = (Book)itemService.findOne(itemId);       //책만 가져온다 하자.  예제 단순화 하기 위해 캐스팅 했다.
        BookForm form = new BookForm();
        form.setId(item.getId());
        form.setName(item.getName());
        form.setPrice(item.getPrice());
        form.setStockQuantity(item.getStockQuantity());
        form.setAuthor(item.getAuthor());
        form.setIsbn(item.getIsbn());
        model.addAttribute("form", form);
        return "items/updateItemForm";
        //form을 업데이트 하는데 BookForm을 보낼거다.

    }

//    /** 웬만하면 setter 쓰지 말고 엔티티에 change()같은 메서드를 따로 만들어서 사용하자.*/
//    @PostMapping("/items/{itemId}/edit")
//    public String updateItem(@ModelAttribute("form")BookForm form,@PathVariable Long itemId) {
//        Book book = new Book();     //form으로 넘어온 객체는 이전에 한번 JPA에 의해서 DB에 저장되었던 애다.
//        book.setIsbn(form.getIsbn());
//        book.setName(form.getName());
//        book.setPrice(form.getPrice());
//        book.setStockQuantity(form.getStockQuantity());
//        book.setId(form.getId());
//        book.setAuthor(form.getAuthor());
//        //위의 코드들은 내가 직접 new로 해서 만든것이다. JPA가 기본적으로 관리를 당연히 안하겠지.. 그래서 값을 바꿔치기 해도 DB에 업데이트가 안일어난다.
//
//        itemService.saveItem(book);     /** 지금 업데이트를 하기위해 book 엔티티를 넘겼다 */
//        return "redirect:/items";
//    }

    /** 위처럼 어설프게 book으로 넘기지 말자. 강의자료 89 page
     *  If, 업데이트 할 데이터가 많다 그럼 따로 DTO를 만들자.
     * */
    @PostMapping("/items/{itemId}/edit")
    public String updateItem(@ModelAttribute("form")BookForm form,@PathVariable Long itemId) {
        //데이터중 3개만 업데이트 한다고 치자.  이렇게 하거나
        //itemService.updateItem(itemId,form.getName(),form.getPrice(),form.getStockQuantity());

        log.info("업데이트 로직");

        //DTO로 따로 해서 한다.
        UpdateItemDto updateItemDto = new UpdateItemDto();
        updateItemDto.setAuthor(form.getAuthor());
        updateItemDto.setIsbn(form.getIsbn());

        updateItemDto.setName(form.getName());
        updateItemDto.setPrice(form.getPrice());
        updateItemDto.setStockQuantity(form.getStockQuantity());

        itemService.updateItem(itemId,updateItemDto);
        return "redirect:/items";
    }


}
