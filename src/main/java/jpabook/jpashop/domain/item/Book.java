package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorColumn;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B")//싱글테이블이라 저장할때 구분할수있게 설정 - 안쓰면 클래스명으로 들어감
@Getter
@Setter
public class Book extends Item{
    private String author;
    private String isbn;
}
