package jpabook.jpashop.domain;

import lombok.Getter;

import javax.persistence.Embeddable;

@Embeddable  // JPA의 내장타입 값 으로 어딘가에 내장(저장) 될수있다
@Getter  // getter 만 열어둔다
public class Address {

    private String city;
    private String street;
    private String zipcode;

    //기본생성자를 만들어줘야함 JPA 특성상
    protected Address(){

    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
