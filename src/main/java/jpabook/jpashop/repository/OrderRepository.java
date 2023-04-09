package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order) {
        em.persist(order);
    }

    public Order findOne(Long id) {
        return em.find(Order.class,id);
    }

  public List<Order> findAllByString(OrderSearch orderSearch){
//      return em.createQuery("select o from Order o join o.member m" +
//                      " where o.status = :status " +
//                      " and m.name like :name", Order.class)
//              .setParameter("status", orderSearch.getOrderStatus())
//              .setParameter("name", orderSearch.getMemberName())
////              .setFirstResult(100) 시작 페이징
//              .setMaxResults(1000) // 최대 1000건
//              .getResultList();

      String jpql = "select o from Order o join o.member m"; //동적쿼리 주문이든 취소든 다 들고오면
      boolean isFirstCondition = true;

      //주문 상태 검색
      if (orderSearch.getOrderStatus() != null) {
          if (isFirstCondition) {
              jpql += " where";
              isFirstCondition = false;
          } else {
              jpql += " and";
          }
          jpql += " o.status = :status";
      }

      //회원 이름 검색
      if (StringUtils.hasText(orderSearch.getMemberName())) {
          if (isFirstCondition) {
              jpql += " where";
              isFirstCondition = false;
          } else {
              jpql += " and";
          }
          jpql += " m.name like :name";
      }

      TypedQuery<Order> query = em.createQuery(jpql, Order.class)
              .setMaxResults(1000);

      if (orderSearch.getOrderStatus() != null) {
          query = query.setParameter("status", orderSearch.getOrderStatus());
      }
      if (StringUtils.hasText(orderSearch.getMemberName())) {
          query = query.setParameter("name", orderSearch.getMemberName());
      }
      return query.getResultList();
  }

  // JPA Criteria
  // 안 쓰는게 좋다 유지보수에 진짜 안 좋다.. 이런거 땜에 queryDSL을 써야한다.
  public List<Order> findAllByCriteria(OrderSearch orderSearch) {
      CriteriaBuilder cb = em.getCriteriaBuilder();
      CriteriaQuery<Order> cq = cb.createQuery(Order.class);
      Root<Order> o = cq.from(Order.class);
      Join<Object, Object> m = o.join("member", JoinType.INNER);

      List<Predicate> criteria = new ArrayList<>();

      // 주문 상태 검색
      if(orderSearch.getOrderStatus() != null) {
          Predicate status = cb.equal(o.get("status"), orderSearch.getOrderStatus());
          criteria.add(status);
      }
      //회원 이름 검색
      if (StringUtils.hasText(orderSearch.getMemberName())) {
          Predicate name =
                  cb.like(m.<String>get("name"), "%" +
                          orderSearch.getMemberName() + "%");
          criteria.add(name);
      }

      cq.where(cb.and(criteria.toArray(new Predicate[criteria.size()])));
      TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000);
      return query.getResultList();
  }

  // queryDSL 관련된 코드
//    public List<Order> findAllByQuerydsl(OrderSearch orderSearch) {
//
//        return query
//                .select(order)
//                .from(order)
//                .join(order.member, member)
//                .where(statusEq(orderSearch.getOrderStatus()),
//                        nameLike(orderSearch.getMemberName()))
//                .limit(1000)
//                .fetch();
//    }
//
//    private BooleanExpression statusEq(OrderStatus statusCond) {
//        if(statusCond == null) {
//            return null;
//        }
//        return order.status.eq(statusCond);
//    }
//
//    private BooleanExpression nameLike(String nameCond) {
//        if(!StringUtils.hasText(nameCond)) {
//            return null;
//        }
//        return member.name.like(nameCond);
//    }
}
