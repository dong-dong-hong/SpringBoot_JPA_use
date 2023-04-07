package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.Extension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.swing.text.html.parser.Entity;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class MemberServiceTest {
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    // 회원 가입
    @Test
    @Rollback(false)
    void joinMember() throws Exception {
        // given
        Member member = new Member();
        member.setName("son");
        // when
        Long saveId = memberService.join(member);
        // then
//        em.flush(); // db에 반영됨  + @Transactional 때문에 롤백도 됨 -> DB에 데이터가 남은 안된다.
        assertEquals(member, memberRepository.findOne(saveId));
    }
    // given -> when -> then : 준비 -> 실행 -> 검증
    // 중복 회원 예외
    @Test
    void DuplicateMemberException() throws Exception {
        // given
        Member member1 = new Member();
        member1.setName("son");

        Member member2 = new Member();
        member2.setName("son");
        // when
        memberService.join(member1);
//        try{
//            memberService.join(member2); // 예외가 발생해야 한다.
//        }catch (IllegalStateException e) {
//            return;
//        }
        // then
//        fail("예외가 발생해야 한다.");
        assertThrows(IllegalStateException.class, () -> {
            memberService.join(member2);
        });
    }
}

// 기술 설명
//@RunWith(SpringRunner.class) : 스프링과 테스트 통합 2점대 버전
//@SpringBootTest : 스프링 부트 띄우고 테스트(이게 없으면 @Autowired 다 실패)
//@Transactional : 반복 가능한 테스트 지원, 각각의 테스트를 실행할 때마다 트랜잭션을 시작하고
//테스트가 끝나면 트랜잭션을 강제로 롤백 (이 어노테이션이 테스트 케이스에서 사용될 때만 롤백)