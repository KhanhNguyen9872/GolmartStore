package com.springboot.dev_spring_boot_demo.dao;

import com.springboot.dev_spring_boot_demo.dao.CartInfoDAO;
import com.springboot.dev_spring_boot_demo.entity.CartInfo;
import com.springboot.dev_spring_boot_demo.entity.CartInfoId;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class CartInfoDAOImpl implements CartInfoDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<CartInfo> findByCartId(Long cartId) {
        TypedQuery<CartInfo> query = entityManager.createQuery(
                "SELECT ci FROM CartInfo ci WHERE ci.id.cartId = :cartId", CartInfo.class
        );
        query.setParameter("cartId", cartId);
        return query.getResultList();
    }

    @Override
    public CartInfo findByCartIdAndProductId(Long cartId, Long productId) {
        CartInfoId id = new CartInfoId(cartId, productId);
        return entityManager.find(CartInfo.class, id);
    }

    @Override
    @Transactional
    public void save(CartInfo cartInfo) {
        if (cartInfo.getId() == null) {
            entityManager.persist(cartInfo);
        } else {
            entityManager.merge(cartInfo);
        }
    }

    @Override
    @Transactional
    public void delete(CartInfo cartInfo) {
        if (entityManager.contains(cartInfo)) {
            entityManager.remove(cartInfo);
        } else {
            entityManager.remove(entityManager.merge(cartInfo));
        }
    }
}