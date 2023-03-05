package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) //JPA 가 DB 까지 들어가는 모습을 보기 위해 메모리 모드로 엮어서 테스트진행
@SpringBootTest
@Transactional // 롤백이 가능하게 기본 설정 되어있음// - JPA에서 같은 Transaction 에서 같은 엔티티 pk 값을 가지면 하나의 영속성 컨텍스트에서 관리된다
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

    //롤백 하지만 insert 쿼리 날리는 방법
    @Autowired
    EntityManager em;

    @Test
//    @Rollback(false)
    public void 회원가입() throws Exception{
        //given - 주어진값
        Member member = new Member();
        member.setName("Hyun");

        //when - 실행하면
        //db 트렌젝션이 커밋 될때 플러시가 되면서 db 인서트 쿼리가 실행된다
        // - 현재는 insert 가 안나가고 memberRepository 의 save 메서드에서 영속화만 해둔 상태다
        // spring 의 test 클래스의 Transactional 은 commit을 안하고 rollback을 해버려 false로 설정 해주자
        Long saveId = memberService.join(member);

        //then - 결과
        em.flush(); //영속성 컨텍스트에 있는 등록내용을 db에 반영시키는 메소드 - 이후 롤백백

       //member 가 repository 에서 찾는다 -> saveId 와 같은 id 를 가진 값을 가져와 equal 비교
        assertEquals(member, memberRepository.findOne(saveId));

    }

    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception{
        //given
        Member member1 =new Member();
        member1.setName("Hyun");

        Member member2 =new Member();
        member2.setName("Hyun");

        //when
        memberService.join(member1);
        memberService.join(member2); //예외가 발생해야 한다
        
        //test 어노테이션에 쓸수있다
//        try {
//            memberService.join(member2); //예외가 발생해야 한다
//        } catch (IllegalStateException e) {
//            return;
//        }

        //then
        fail("예외가 발생해야 한다."); //junit 기본 제공되는 assert 메소드 - 여기까지오면 테스트 실패
    }
}