package jpabook.jpabook.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    /** 값 타입은 변경이 되면 안된다. 그래서 생성할 때만 값이 세팅이 되고 Getter만 제공한다. */
    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
    //JPA 스팩에서는  protected까지 허용 해준다
    protected Address() {
    }
}
