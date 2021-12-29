package com.hewentian.shardingsphere.springboot.jpa.repository;

import com.hewentian.shardingsphere.springboot.jpa.entity.Address;
import com.hewentian.shardingsphere.springboot.jpa.entity.Order;
import com.hewentian.shardingsphere.springboot.jpa.entity.OrderItem;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.util.List;

@Repository
@Transactional
public class JpaRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public Long insertOrder(final Order order) {
        entityManager.persist(order);
        return order.getOrderId();
    }

    public Long insertOrderItem(final OrderItem orderItem) {
        entityManager.persist(orderItem);
        return orderItem.getOrderItemId();
    }

    public Long insertAddress(final Address address) {
        entityManager.persist(address);
        return address.getAddressId();
    }

    public List<Order> selectAllOrder() {
        return (List<Order>) entityManager.createQuery("SELECT o FROM Order o").getResultList();
    }

    public List<OrderItem> selectAllOrderItem() {
        return (List<OrderItem>) entityManager.createQuery("SELECT o from OrderItem o").getResultList();
    }

    public List<Address> selectAllAddress() {
        return (List<Address>) entityManager.createQuery("SELECT o FROM Address o").getResultList();
    }

    public void deleteOrder(final Long orderId) {
        Query query = entityManager.createQuery("DELETE FROM Order o WHERE o.orderId = ?1");
        query.setParameter(1, orderId);
        query.executeUpdate();
    }

    public void deleteOrderItem(final Long orderId) {
        Query query = entityManager.createQuery("DELETE FROM OrderItem i WHERE i.orderId = ?1");
        query.setParameter(1, orderId);
        query.executeUpdate();
    }
}
