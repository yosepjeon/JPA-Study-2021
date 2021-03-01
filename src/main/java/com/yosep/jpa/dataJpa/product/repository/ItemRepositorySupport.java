package com.yosep.jpa.dataJpa.product.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yosep.jpa.dataJpa.product.data.entity.Item;
import com.yosep.jpa.dataJpa.product.data.entity.QItem;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ItemRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;
    QItem item = QItem.item;

    public ItemRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(Item.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<Item> findAll() {
        return jpaQueryFactory
                .selectFrom(item)
                .fetch();
    }
}
