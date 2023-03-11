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


    //== 생성 메서드 ==//
    //item 종류, 주문당시 가격, 개수
    public static OrderItem createOrderItem(Item item, int orderPrice, int count){
        OrderItem orderItem = new OrderItem();
        orderItem.setItem(item);
        orderItem.setOrderPrice(orderPrice);
        orderItem.setCount(count);

        item.removeStock(count); //재고 감소
        return orderItem;
    }

    //== 비즈니스 로직 ==//
    public void cancel(){
        getItem().addStock(count); // 몇개를 주문했었는지 데이터를 넣어줘서 재고 에 더해준다
        //상태는 부모클래스에서 참고할수있어서 새략

    }
    //==조회 로직==//
    /*
    * 주문상품 전체 가격 조회
    * */
    public int getTotalPrice(){
        return getOrderPrice() * getCount();
    }

}
