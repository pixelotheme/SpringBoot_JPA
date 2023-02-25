package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("A")//싱글테이블이라 저장할때 구분할수있게 설정 - 안쓰면 클래스명으로 들어감
@Getter
@Setter
public class Album extends Item{

    private String artist;
    private String etc;
}
