package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {
//    @Autowired
//    @PersistenceContext
    private final EntityManager em;

//    @PersistenceUnit
//    private EntityManagerFactory emf;

//    public MemberRepository(EntityManager em) {
//        this.em = em;
//    }

    public void save(Member member) {
        em.persist(member);
    }
    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        return em.createQuery("select m from jpabook.jpashop.domain.Member m where m.name= : name", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from jpabook.jpashop.domain.Member m where m.name= : name" , Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
// 기술 설명
//@Repository : 스프링 빈으로 등록, JPA 예외를 스프링 기반 예외로 예외 변환
//@PersistenceContext : 엔티티 메니저( EntityManager ) 주입
//@PersistenceUnit : 엔티티 메니터 팩토리( EntityManagerFactory ) 주입
