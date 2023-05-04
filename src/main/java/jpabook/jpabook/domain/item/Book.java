package jpabook.jpabook.domain.item;

import jpabook.jpabook.domain.service.UpdateItemDto;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Slf4j
@Entity
@Getter @Setter
@DiscriminatorValue("B")
public class Book extends Item{
    private String author;
    private String isbn;

    @Override
    public void changeItem(UpdateItemDto updateItemDto) {
        log.info("Book Entity 업데이트 로직");
        super.changeItem(updateItemDto);
        this.author = updateItemDto.getAuthor();
        this.isbn = updateItemDto.getIsbn();
    }
}
