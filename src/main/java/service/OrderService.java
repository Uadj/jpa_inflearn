package service;

import domain.Delivery;
import domain.DeliveryStatus;
import Entity.Member;
import domain.Order;
import domain.OrderItem;
import domain.OrderSearch;
import domain.Item;
import repository.ItemRepository;
import repository.MemberRepository;
import repository.OrderRepository;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
     private final MemberRepository memberRepository;
     private final OrderRepository orderRepository;
     private final ItemRepository itemRepository;
     /** 주문 */
     @Transactional
     public Long order(Long memberId, Long itemId, int count) {
     //엔티티 조회
     Member member = memberRepository.findOne(memberId);
     Item item = itemRepository.findOne(itemId);
     //배송정보 생성
     Delivery delivery = new Delivery();
     delivery.setAddress(member.getAddress());
     delivery.setStatus(DeliveryStatus.READY);
     //주문상품 생성
     OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(),
                                                     count);
     //주문 생성
     Order order = Order.createOrder(member, delivery, orderItem);
     //주문 저장
     orderRepository.save(order);    
     return order.getId();
     }
     /** 주문 취소 */
     @Transactional
     public void cancelOrder(Long orderId) {
     //주문 엔티티 조회
     Order order = orderRepository.findOne(orderId);
     //주문 취소
     order.cancel();
     }
     /** 주문 검색 */
    /*
     public List<Order> findOrders(OrderSearch orderSearch) {
     return orderRepository.findAll(orderSearch);
     }
     
    */
//     public List<Order> findAllByString(OrderSearch orderSearch) {
//      //language=JPAQL
//      String jpql = "select o From Order o join o.member m";
//      boolean isFirstCondition = true;
//      //주문 상태 검색
//      if (orderSearch.getOrderStatus() != null) {
//      if (isFirstCondition) {
//          jpql += " where";
//          isFirstCondition = false;
//      } else {
//          jpql += " and";
//      }
//          jpql += " o.status = :status";
//      }
//      //회원 이름 검색
//      if (StringUtils.hasText(orderSearch.getMemberName())) {
//      if (isFirstCondition) {
//          jpql += " where";
//          isFirstCondition = false;
//      } else {
//          jpql += " and";
//      }
//          jpql += " m.name like :name";
//      }
//         TypedQuery<Order> query = em.createQuery(jpql, Order.class)
//      .setMaxResults(1000); //최대 1000건
//      if (orderSearch.getOrderStatus() != null) {
//      query = query.setParameter("status", orderSearch.getOrderStatus());
//      }
//      if (StringUtils.hasText(orderSearch.getMemberName())) {
//      query = query.setParameter("name", orderSearch.getMemberName());
//      }
//      return query.getResultList();
//     }
    
//     public List<Order> findAllByCriteria(OrderSearch orderSearch) {
//      CriteriaBuilder cb = em.getCriteriaBuilder();
//      CriteriaQuery<Order> cq = cb.createQuery(Order.class);
//      Root<Order> o = cq.from(Order.class);
//      Join<Order, Member> m = o.join("member", JoinType.INNER); //회원과 조인
//      List<Predicate> criteria = new ArrayList<>();
//      //주문 상태 검색
//      if (orderSearch.getOrderStatus() != null) {
//      Predicate status = cb.equal(o.get("status"),
//     orderSearch.getOrderStatus());
//      criteria.add(status);
//      }
//      //회원 이름 검색
//      if (StringUtils.hasText(orderSearch.getMemberName())) {
//      Predicate name =
//      cb.like(m.<String>get("name"), "%" +
//     orderSearch.getMemberName() + "%");
//      criteria.add(name);
//          }
//      cq.where(cb.and(Criteria.toArray(new Predicate[criteria.size()])));
//      TypedQuery<Order> query = em.createQuery(cq).setMaxResults(1000); //최대 1000건
//      return query.getResultList();
// }
}