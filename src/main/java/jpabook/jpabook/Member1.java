package jpabook.jpabook;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter @Setter
public class Member1 {

    @Id
    @GeneratedValue
    private Long id;
    private String username;



}
