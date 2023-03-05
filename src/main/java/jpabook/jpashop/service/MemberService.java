package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service // Spring 서비스 어노테이션 - 컴포넌트 스캔의 대상이되어 스프링 빈으로 자동 등록
// jpa가 조회하는 곳에서는 성능을 최적화 시켜준다
@Transactional(readOnly = true) //jpa의 모든 데이터 변경, 로직들은 가급적 Transactional 안에서 실행 되어야한다 - Lazy 로딩 ... 이 가능하다
//@AllArgsConstructor // 롬북 어노테이션 - 필드로 선언된 (memberRepository)를 Autowired로 자동으로 모두 주입시켜준다
@RequiredArgsConstructor //final 필드만 생성자를 만들어주고 injection 해준다
public class MemberService {

    //필드 injection 이라고 한다
//    @Autowired // 자동 으로 인젝션 된다 - spring이 springBean에 등록되어 있는 MemberRepository를 자동 인젝션 해준다
    private final MemberRepository memberRepository; //컴파일 시점에 값이 안들어오면 바로 에러 뜬다

//    @Autowired // setter injection 테스트 할때 편함 - 누구나 setter 쓸수있어서 안된다
//    public void setMemberRepository(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    //@AllArgsConstructor 를 사용하면 아래와 같이 안써도 생성해준다
//    @Autowired // 생성자 injection - 최신버전은 생성자가 하나만 있는경우 Autowired 로 적용해준다
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

//    public static void main(String[] args){
//        MemberService memberService = new MemberService(필요한 객체 넣어준다);
//    }


    /**
     * 회원 가입
     * */
    @Transactional // 데이터 변경해야하니 readOnly = true는 쓰지 않는다 - 기본값이 false
    public Long join(Member member){
        validateDuplicateMember(member);
        memberRepository.save(member); //저장
        return member.getId();
    }
    //중복 회원시 예외 발생
    private void validateDuplicateMember(Member member) {
        //멤버를 가져왔을때 개수가 0보다 크면 예외발생 으로 해도 간단히 가능하다
        //was 에 동시 접속되어 insert를 하게되면 동시에 메서드를 호출할수 있기때문에(멀티쓰레드 상황)
        //member의 Name을 유니크 제약조건을 걸어 해결해준다
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()){
            throw new IllegalStateException("이미 존재하는 회원입니다");
        }
    }


    //회원 전체 조회
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }
    //id 로 단건 조회
    public Member findOne(Long memberId){
        return memberRepository.findOne(memberId);

    }

}
