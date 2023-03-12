package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /*
    * 주문 -
    */
    @Transactional // 데이터 변경
    public Long order(Long memberId, Long itemId, int count){
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성 - 회원의 주소 값을 넣어줄것
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문상품 생성 - 생성이라고 new 로 생성하여 값을 세팅하지 말고 항상 제약하는 형태로
        // 만들어야 좋은 설계와 유지보수성을 갖는다
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //직접 생성하여 값을 넣게 될 경우 유지보수의 어려움이 있다 - 생성하는것을 막는다
        //OrderItem 참고
//        OrderItem orderItem1 = new OrderItem();
//        orderItem1.setOrder("!");

        //주문 생성 - 회원정보, 주문지, 주문한 상품 정보
        Order order = Order.createOrder(member, delivery, orderItem);

        //주문 저장  - Order 엔티티 에서 CasecadeType.ALL 옵션 때문에
        //order 를 persist 하면 옵션이 걸려있는 곳은 같이 저장된다(orderItem, delivery)
        orderRepository.save(order);

        return order.getId();
    }

    /*
    * 주문 취소
    *  */
    @Transactional
    public void cancelOrder(Long orderId){
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        //주문 취소 - 비즈니스 로직은 엔티티에 있다
        order.cancel();
        // mybatis,jdbcTemple 같은경우 밖에서 직접  update 쿼리를 짜서 넘겨줘야 했다
        //엔티티 안에서 데이터만 바꾸면 JPA가 DirtyChecking(데이터 변경감지)을 통해
        //변경된 부분은 찾아 DB에 update 쿼리가 자동으로 나라간다
        //- save 할때 em.persist 했던것 조차도 안해줘도 된다
    }

    //검색
    /*public List<Order> findOrders(OrderSearch orderSearch){
        return orderRepository.findAll(orderSearch);
    }*/
}


