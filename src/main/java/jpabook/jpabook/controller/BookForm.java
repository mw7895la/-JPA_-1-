package jpabook.jpabook.controller;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookForm {
    private Long id;

    private String name;
    private int price;
    private int stockQuantity;
    //여기까지 상품 공통 속성.

    private String author;
    private String isbn;

}
