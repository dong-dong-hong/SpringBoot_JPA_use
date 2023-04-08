package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired EntityManager em;
    @Autowired OrderService orderService;
    @Autowired OrderRepository orderRepository;
    @Test
    @DisplayName("상품주문")
    public void productOrder() throws Exception {
        // given
        Member member = createMember();

        Item item = createBook("김영한의 jpa",30000,100);// 이름, 가격, 재고
        int orderCount = 10;
        // when
        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);
        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문 시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1, getOrder.getOrderItems().size());
        assertEquals("주문 가격은 가격 * 수량이다.", 30000 * orderCount, getOrder.getTotalPrice());
        assertEquals("주문 수량만큼 재고가 줄어야 한다.", 90, item.getStockQuantity());
    }

    @Test
    @DisplayName("상품주문 재고 수량 초과")
    public void productOrderInventoryQuantityExceeded() throws Exception {
        // given
        Member member = createMember();
        Book book= createBook("백엔드 개발자", 20000,50);

        int orderCount = 100; // 재고보다 많은 수량
        // when
        // then
        assertThrows(NotEnoughStockException.class, () -> {
            orderService.order(member.getId(), book.getId(), orderCount);
        }, "재고 수량 예외가 발생해야 한다.");
//        fail("재고 수량 부족 예외가 발생해야 한다."); // 호출 시점에서 강제로 테스트가 실패한다.
    }

    @Test
    @DisplayName("주문취소")
    public void orderCancel() throws Exception {
        // given
        Member member = createMember();
        Book item = createBook("취업하자", 50000, 200);

        int orderCount = 100;

        Long orderId = orderService.order(member.getId(), item.getId(), orderCount);

        // when
        orderService.cancelOrder(orderId);
        // then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("주문 취소 시 상태는 CANCEL이다.", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.", 200, item.getStockQuantity());
    }
    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울시","마포구","130-1"));
        em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setPrice(price);
        book.setStockQuantity(stockQuantity);
        em.persist(book);
        return book;
    }
}