package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class OrderItem {

    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne // 항상 클래스명을 기준으로 선언하는 타입 의 클래스와 무슨관계인가 생각
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")//하나의 order가 여러개 order_item 을 가질수 있다 (연관관계의 주인 order_item)
    private Order order;

    private int orderPrice; //주문당시 가격
    private int count; // 주문수량
}
