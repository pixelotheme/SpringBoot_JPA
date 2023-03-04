package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id") //실제 테이블의 컬럼명으로
    private Long id;

    //order 와 member 는 다대일 관계 (order의 입장 다수의 주문이 한개의 아이디에 매핑)
//    @ManyToOne(fetch = FetchType.EAGER) // Default 설정이다 - Order를 조회할때 member를 조인으로 모두 가져온다
    @ManyToOne(fetch = LAZY) //
    @JoinColumn(name = "member_id") // 매핑을 무엇으로 할것인가
    private Member member;

//    @ManyToOne(fetch = FetchType.EAGER) 로 설정 되어 있다면
    //JPQL select o from order o; -> Sql로 번역될때 select * from order 로 모두 다가져온다 -> member까지 모두 다가져온다
    //n + 1 문제가 발생 - 첫번째 날린 쿼리의 결과 order = n 100개 가나왔다-> member에서 단권쿼리가 100 + 1(order 처음 조회 쿼리)번을 조회한다

    //cascade All -> order 를 저장하면 orderItems 도 같이 저장된다
    //원래는 persist 를 orderItem에도 걸어줘야한다
//    persist(orderIteamA)
//    persist(orderIteamB)
//    persist(orderIteamC)
//    persist(order)
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) // default , fetch = FetchType.LAZY
    private List<OrderItem> orderItems = new ArrayList<>();

    //하나의 주문은 하나의 배송정보만 가져가야한다
    //JPA 는 FK 를 둘중 아무나 줄수있는데 자주사용하는 쪽에다 넣는다
    //주문을 보면서 배송지를 볼경우가 많으니 orders 테이블에 둔다(연관관계의 주인)
    @OneToOne(fetch = LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")// 실제 데이터 삽입용
    private Delivery delivery;

//    private Date date; //  날짜관련 어노테이션 설정 해줬어야했음
    private LocalDateTime orderDate; //java8 - 분까지 있다, java 8은 Hibernate 가 자동으로 지원해준다

    //ORDINAL - 숫자로 들어감(기본설정) 1 2 3 .... 절대 쓰면안된다 마음대로 숫자가 들어간다
    //STIRNG - 설정한 문자열로 그대로 들어가게 해야한다
    @Enumerated(EnumType.STRING)
    private OrderStatus status;  // 주문상태 ENUM 생성 [OREDR, CANCEL]

    //== 연관관계 편의 메서드 == 위치는 핵심적으로 컨트롤 하는곳에 있으면 좋다
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    //원래대로 라면 이렇게 직접 넣어준다
//    public static void main(String[] arg){
//        Member member = new Member();
//        Order order = new Order();
//
//        member.getOrders().add(order);
//        order.setMember(member);
//    }

    public void addOrderItem(OrderItem orderItem){
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

}
