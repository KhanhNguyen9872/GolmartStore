package com.springboot.dev_spring_boot_demo.dao;

import com.springboot.dev_spring_boot_demo.entity.Admin;
import com.springboot.dev_spring_boot_demo.entity.Authority;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class AdminDAOImpl implements AdminDAO {

    private final EntityManager em;

    @Autowired
    public AdminDAOImpl(EntityManager em) {
        this.em = em;
    }

    @Override
    public List<Admin> findAll() {
        TypedQuery<Admin> query = em.createQuery("from Admin", Admin.class);
        return query.getResultList();
    }

    @Override
    public Page<Admin> findAll(Pageable pageable) {
        TypedQuery<Admin> query = em.createQuery("from Admin", Admin.class);
        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());
        List<Admin> admins = query.getResultList();

        TypedQuery<Long> countQuery = em.createQuery("select count(a) from Admin a", Long.class);
        Long count = countQuery.getSingleResult();

        return new PageImpl<>(admins, pageable, count);
    }

    @Override
    public Admin findById(Long id) {
        return em.find(Admin.class, id);
    }

    @Override
    public Admin save(Admin admin) {
        return em.merge(admin);
    }

    @Override
    @Transactional
    public void deleteById(Long id) {
        Admin admin = em.find(Admin.class, id);
        if (admin != null) {
            em.remove(admin);
        }
    }

    @Override
    public Admin findByUsername(String username) {
        TypedQuery<Admin> query = em.createQuery("from Admin where username = :username", Admin.class);
        query.setParameter("username", username);
        List<Admin> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public List<String> findAuthoritiesByUsername(String username) {
        TypedQuery<String> query = em.createQuery(
                "SELECT a.authority FROM Authority a WHERE a.username = :username",
                String.class
        );
        query.setParameter("username", username);
        return query.getResultList();
    }

    @Override
    @Transactional
    public void deleteAuthoritiesByUsername(String username) {
        em.createQuery("DELETE FROM Authority a WHERE a.username = :username")
                .setParameter("username", username)
                .executeUpdate();
    }

    @Override
    @Transactional
    public void saveAuthority(Authority authority) {
        em.persist(authority);
    }
}
