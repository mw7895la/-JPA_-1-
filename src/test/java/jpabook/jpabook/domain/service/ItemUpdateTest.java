package jpabook.jpabook.domain.service;

import jpabook.jpabook.domain.item.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemUpdateTest {

    @Autowired
    EntityManager em;

    @Test
    public void updateTest() {
        Book book = em.find(Book.class, 1L);

        //TX
        book.setName("asdfasdf");
        //이러면 이제 최종커밋전에 바뀐 데이터가 있으니 업데이트 쿼리를 쿼리저장소에 쌓고 커밋 전에 쿼리 날린다. 이게 "변경감지 - dirty checking"
        //TX commit

    }
}
