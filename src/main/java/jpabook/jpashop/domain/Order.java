package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id") //실제 테이블의 컬럼명으로
    private Long id;

    //order 와 member 는 다대일 관계 (order의 입장 다수의 주문이 한개의 아이디에 매핑)
    @ManyToOne
    @JoinColumn(name = "member_id") // 매핑을 무엇으로 할것인가
    private Member member;

    @OneToMany(mappedBy = "order")
    private List<OrderItem> orderItems = new ArrayList<>();

    //하나의 주문은 하나의 배송정보만 가져가야한다
    //JPA 는 FK 를 둘중 아무나 줄수있는데 자주사용하는 쪽에다 넣는다
    //주문을 보면서 배송지를 볼경우가 많으니 orders 테이블에 둔다(연관관계의 주인)
    @OneToOne
    @JoinColumn(name = "delivery_id")// 실제 데이터 삽입용
    private Delivery delivery;

//    private Date date; //  날짜관련 어노테이션 설정 해줬어야했음
    private LocalDateTime orderDate; //java8 - 분까지 있다, java 8은 Hibernate 가 자동으로 지원해준다

    //ORDINAL - 숫자로 들어감(기본설정) 1 2 3 .... 절대 쓰면안된다 마음대로 숫자가 들어간다
    //STIRNG - 설정한 문자열로 그대로 들어가게 해야한다
    @Enumerated(EnumType.STRING)
    private OrderStatus status;  // 주문상태 ENUM 생성 [OREDR, CANCEL]

}
