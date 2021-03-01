package com.yosep.jpa.dataJpa.product.repository;

import com.yosep.jpa.dataJpa.product.data.entity.Item;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
    private final EntityManager em;

    public void save(Item item) {
        if(em.find(Item.class, item.getId()) ==null) {
            em.persist(item);
        }else {
            em.merge(item);
        }
    }

    public Item findOne(long id) {
        return em.find(Item.class, id);
    }
}
