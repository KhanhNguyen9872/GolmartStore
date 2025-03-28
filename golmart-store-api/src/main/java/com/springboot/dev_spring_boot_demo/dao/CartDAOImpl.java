package com.springboot.dev_spring_boot_demo.dao;

import com.springboot.dev_spring_boot_demo.entity.Cart;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class CartDAOImpl implements CartDAO {

    private final EntityManager entityManager;

    @Autowired
    public CartDAOImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Cart findByUserId(Long userId) {
        TypedQuery<Cart> query = entityManager.createQuery(
                "SELECT c FROM Cart c WHERE c.user.id = :userId", Cart.class
        );
        query.setParameter("userId", userId);
        return query.getResultStream().findFirst().orElse(null);
    }

    @Override
    @Transactional
    public void save(Cart cart) {
        entityManager.merge(cart);
    }

    @Override
    @Transactional
    public void delete(Cart cart) {
        if (entityManager.contains(cart)) {
            entityManager.remove(cart);
        } else {
            entityManager.remove(entityManager.merge(cart));
        }
    }

    @Override
    public Cart findById(Long id) {
        return entityManager.find(Cart.class, id);
    }
}