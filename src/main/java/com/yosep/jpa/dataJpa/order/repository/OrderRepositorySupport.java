package com.yosep.jpa.dataJpa.order.repository;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yosep.jpa.dataJpa.order.data.dto.OrderSearchDto;
import com.yosep.jpa.dataJpa.order.data.entity.Order;
import com.yosep.jpa.dataJpa.order.data.entity.QOrder;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

@Repository
public class OrderRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;
    QOrder order = QOrder.order;

    public OrderRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(Order.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<Order> findAll(OrderSearchDto orderSearchDto) {
        if (orderSearchDto.getOrderStatus() != null && StringUtils.hasText(orderSearchDto.getMemberName())) {

        }

        return jpaQueryFactory
                .selectFrom(order)
                .where(
                        getWhereMemberNameParameter(orderSearchDto),
                        getWhereOrderStatusParameter(orderSearchDto)
                )
                .fetch();
    }

    private BooleanExpression getWhereMemberNameParameter(OrderSearchDto orderSearchDto) {
        return StringUtils.hasText(orderSearchDto.getMemberName()) ? order.member.name.like(orderSearchDto.getMemberName()) : null;
    }

    private BooleanExpression getWhereOrderStatusParameter(OrderSearchDto orderSearchDto) {
        return orderSearchDto.getOrderStatus() != null ? order.status.eq(orderSearchDto.getOrderStatus()) : null;
    }
}
