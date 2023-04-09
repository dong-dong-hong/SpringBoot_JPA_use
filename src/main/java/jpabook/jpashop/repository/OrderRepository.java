package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

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

  public List<Order> findAll(OrderSearch orderSearch){
//      return em.createQuery("select o from Order o join o.member m" +
//                      " where o.status = :status " +
//                      " and m.name like :name", Order.class)
//              .setParameter("status", orderSearch.getOrderStatus())
//              .setParameter("name", orderSearch.getMemberName())
////              .setFirstResult(100) 시작 페이징
//              .setMaxResults(1000) // 최대 1000건
//              .getResultList();

      String jpql = "select o from Order o join o.member m";
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



      return em.createQuery(jpql, Order.class) //동적쿼리 주문이든 취소든 다 들고오면
              .setMaxResults(1000)
              .getResultList();
  }
}
