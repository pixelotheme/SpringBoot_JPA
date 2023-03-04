package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity  //엔티티(테이블)로 선언, 엔티티객체는 테이블의 행
//@Table(name = "") 직접 테이블 명을 지정 할 수 있다
@Getter @Setter
public class Member {

    //식별자 - Id에 매핑, GeneratedValue는 값 자동 생성 - 시퀀스값 같은것을 바로 쓴다
    @Id @GeneratedValue
    @Column(name = "member_id") // 컬럼명이 같은곳으로 테이블에 매핑
    private Long id;

    private String name;

    //내장타입을 포함했다 , 내장타입을 쓸때는 Embedded, Embeddable 둘중 하나만 있어도 되는데 보통 둘다해준다
    @Embedded
    private Address address;

    //하나의 Member에 다수의 Order가 있다, Order에서는 입력
    @OneToMany(mappedBy = "member") // order테이블에 있는 member 필드에 의해 정의된 내용을 보여줄것이다
    private List<Order> orders = new ArrayList<Order>();

}
