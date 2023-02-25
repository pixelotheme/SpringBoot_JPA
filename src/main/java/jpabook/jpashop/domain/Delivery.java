package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {

    @Id
    @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery") //거울용
    private Order order;

    @Embedded
    private Address address;

    //ORDINAL - 숫자로 들어감(기본설정) 1 2 3 .... 절대 쓰면안된다 마음대로 숫자가 들어간다
    //STIRNG - 설정한 문자열로 그대로 들어가게 해야한다
    @Enumerated(EnumType.STRING)
    private DeliveryStatus status; //READY, COMP
}
