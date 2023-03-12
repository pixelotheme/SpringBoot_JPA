package jpabook.jpashop.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.aspectj.weaver.ast.Or;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.FetchType.*;
import static javax.persistence.FetchType.LAZY;

@Entity
@Table(name = "orders")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)// new 로 생성하여 값을 넣을수 없다
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


    //== 생성 메서드 ==// 핵심 비즈니스 로직 넣을것

    //createOrder 하기 전에 OrderItem 생성되고 재고 감소 시킨후 createOrder 시킨다

    //주문 생성 시킬때 생성하면서 로직을 모두 넣어두고 -> 수정시 여기만 로직을 보면 돤다 -
    // 다른곳에서 setter 찍고 하지 않다보니 유지보수가 편하다
    public static Order createOrder(Member member, Delivery delivery, OrderItem... orderItems){
        //order 가 연관관계 를 모드 세팅을해서 리턴해준다
        //생성하는 시점을 변경해야한다면 new Order 부분만 변경시키면 된다
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for(OrderItem orderItem : orderItems){
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER); //처음 상태를 ORDER로 강제한다 - ENUM 으로 만들어둔 값을 사용
        order.setOrderDate(LocalDateTime.now());//현재시간
        return order;

    }

    /*
    * 주문 취소
    * */
    //이미 주문시 취소되는 로직이 엔티티에 있다
    //엔티티 안에서 데이터만 바꾸면 JPA가 DirtyChecking(데이터 변경감지)을 통해 변경된 부분은 찾아 DB에 update 쿼리가 자동으로 나라간다
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);

        for (OrderItem orderItem : orderItems){
            orderItem.cancel(); // orderItem 쪽에 취소를 넣어 수량 변경 -> cancel 메서드에 구현되어있다
        }
    }

    //== 조회 로직 ==//
    /*
    * 전체 주문 가격 조회
    * */
    //getTotalPrice 에 로직 구현되어있다
    public int getTotalPrice(){
//        int totalPrice = 0;
//        for(OrderItem orderItem : orderItems){
//            // 주문 수량 * 주문당시 가격 = 총 가격
//            totalPrice += orderItem.getTotalPrice();
//
//        }
        //해당 타입으로 꺼내온다 -> :: 으로 해당 타입의 메서드에 접근한다 -> mapToInt 로 int 값을 map에 넣는다
        return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
    }
}
