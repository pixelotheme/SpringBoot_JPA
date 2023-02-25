package jpabook.jpashop.domain.item;

import jpabook.jpashop.domain.Category;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
//상속관계 매핑으로 부모테이블에 잡아준다 - 싱글테이블 전략
//SINGLE_TABLE, TABLE_PER_CLASS, JOINED 3가지 가있다
// SINGLE_TABLE - 한개의 테이블에 모두 넣는것
// TABLE_PER_CLASS - book,movie,album 각 테이블로 나누는것
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype") //dtype 컬럼일때 해당 어노테이션 value 를 가져온다
@Getter
@Setter
public abstract class Item { //추상클래스 설정 - 구현체는 따로 만들것

    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    //상속관계
    private String name;
    private int price;
    private int stockQuantity;

    @ManyToMany(mappedBy = "items")
    private List<Category> categories = new ArrayList<>();

}
