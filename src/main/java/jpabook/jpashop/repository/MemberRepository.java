package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.*;
import java.util.List;

//스프링부트는 기본적으로@SpringBootApplication 어노테이션 아래에있는 모든 패키지를 컴포넌트 스캔의 대상이된다
@Repository // 스프링에서 제공되는 리포지토리 어노테이션 사용시 컴포넌트 스캔의 대상이되어 지동으로 빈으로 관리된다
@RequiredArgsConstructor // spring boot 라이브러리의 Data JPA 라이브러리를 사용하기 떄문에 사용 가능 - 생성자인젝션으로 바꿔줌
public class MemberRepository {

    /**
     * EntityManager는 Autowired로는 원래 injection 이 안되고 @PersistenceContext 가 꼭 필요한데
     * spring Data JPA 라이브러리의 기능으로 Autowired로 바로 가능하게 해준것이다
     * 향후에는 spring 기본 라이브러리에서도 지원할 예정
     * */

//    @PersistenceContext //jpa를 상용하기 때문에 jpa 표준 어노테이션 사용 - 생성자 안쓰고
    //@Autowired // spring Data JPA 라이브러리를 사용하면 생성자 인젝션을 해줄수 있다
    private final EntityManager em;// 스프링이 엔티티메니저를 만들어 em 변수에 자동으로 주입(injection)해준다

    //만약 EntityManagerFactory 를 직접 주입 해주고 싶다면 아래와 같이 쓴다 (거의 쓸일은 없다)
//    @PersistenceUnit
//    private EntityManagerFactory emf;

    // 생성자 injection을 해줄수 있다
//    public MemberRepository(EntityManager em) {
//        this.em = em;
//    }

    //jpa가 저장하게 만드는 메서드 - EntityManager 의 persist 메서드 사용하여 영속화 시켜 저장한다
    public void save(Member member){

        em.persist(member); //영속성 컨텍스트에 member를 넣고 트랜젝션이 commit 되는 시점에 db에 insert 가 날라간다
    }

    //find 메서드 사용 - 첫번째 타입 , 두번째 Pk값
    public Member findOne(Long id){
        return em.find(Member.class, id);
    }

    //리스트 조회 - sql 은 테이블을 대상으로 조회 - jpa 엔티티 객체를 대상으로 조회
    public List<Member> findAll(){
        //1번째 - JpaQl, 2번째 - 반환 타입
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }
    //이름에 의한 검색
    public List<Member> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }

}
