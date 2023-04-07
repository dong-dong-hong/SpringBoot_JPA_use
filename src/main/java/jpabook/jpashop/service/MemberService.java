package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

//    @Autowired
    private final MemberRepository memberRepository;

    // final 키워드를 추가하면 컴파일 시점에 memberRepository 를 설정하지 않는 오류를 체크할 수 있다.
    //(보통 기본 생성자를 추가할 때 발견)

//    @Autowired
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    // 회원 가입
    @Transactional // 변경
    public Long join(Member member) {

       validateDuplicateMember(member); // 중복 회원 검증
       memberRepository.save(member);
       return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        //EXCEPTION
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
// 기술 설명
// @Service
// @Transactional : 트랜잭션, 영속성 컨텍스트
// readOnly=true :
// 데이터의 변경이 없는 읽기 전용 메서드에 사용,
// 영속성 컨텍스트를 플러시 하지 않으므로 약간의 성능 향상(읽기 전용에는 다 적용)
// 데이터베이스 드라이버가 지원하면 DB에서 성능 향상
// @Autowired
// 생성자 Injection 많이 사용, 생성자가 하나면 생략 가능

// 실무에서는 검증 로직이 있어도 멀티 쓰레드 상황을 고려해서
// 회원 테이블의 회원명 컬럼에 유니크 제약 조건을 추가하는 것이 안전하다.
