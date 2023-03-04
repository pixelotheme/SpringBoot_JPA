package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    @Column(name = "category_id")
    private Long id;

    private String name;

    //카테고리 와 items와 다대다 관계
    @ManyToMany
    @JoinTable(name = "category_item",
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")
    ) // 카테고리와 아이템 사이의 테이블로 연결 - 실무에서는 안쓴다 필드추가가 불가능 해져서
    private List<Item> items = new ArrayList<>();

    //셀프로 양방향 연관관계를 걸었다 (같은 엔티티에 대하야)
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent; // 내 부모

    @OneToMany(mappedBy = "parent")//부모는 여러 자식 가능
    private List<Category> child = new ArrayList<>();

    //==연관관계 메서드==//
    public void addChildCategory(Category child){
        this.child.add(child);
        child.setParent(this);// 부모가 누군지도 알아야한다
    }

}
