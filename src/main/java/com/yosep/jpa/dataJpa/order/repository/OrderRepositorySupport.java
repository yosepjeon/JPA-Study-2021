package com.yosep.jpa.dataJpa.order.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.yosep.jpa.dataJpa.common.data.entity.QDelivery;
import com.yosep.jpa.dataJpa.member.data.entity.QMember;
import com.yosep.jpa.dataJpa.order.data.dto.OrderItemQueryDto;
import com.yosep.jpa.dataJpa.order.data.dto.OrderQueryDto;
import com.yosep.jpa.dataJpa.order.data.dto.OrderSearchDto;
import com.yosep.jpa.dataJpa.order.data.dto.OrderSimpleQueryDto;
import com.yosep.jpa.dataJpa.order.data.entity.Order;
import com.yosep.jpa.dataJpa.order.data.entity.QOrder;
import com.yosep.jpa.dataJpa.order.data.entity.QOrderItem;
import com.yosep.jpa.dataJpa.product.data.entity.QItem;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
public class OrderRepositorySupport extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;
    QOrder order = QOrder.order;
    QDelivery delivery = QDelivery.delivery;
    QMember member = QMember.member;
    QOrderItem orderItem = QOrderItem.orderItem;
    QItem item = QItem.item;

    public OrderRepositorySupport(JPAQueryFactory jpaQueryFactory) {
        super(Order.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public List<Order> findAll() {

        return jpaQueryFactory
                .selectFrom(order)
                .fetch();
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

    public List<Order> findAllWithMemberDelivery() {
        return jpaQueryFactory
                .selectFrom(order)
                .leftJoin(order.delivery, delivery).fetchJoin()
                .leftJoin(order.member, member).fetchJoin()
                .fetch();
    }

    public List<Order> findAllWithMemberDelivery(int offset, int limit) {
        return jpaQueryFactory
                .selectFrom(order)
                .leftJoin(order.delivery, delivery).fetchJoin()
                .leftJoin(order.member, member).fetchJoin()
                .offset(offset)
                .limit(limit)
                .fetch();
    }

    public List<OrderSimpleQueryDto> findOrderDtos() {
        return jpaQueryFactory
                .selectFrom(order)
                .leftJoin(order.delivery, delivery).fetchJoin()
                .leftJoin(order.member, member).fetchJoin()
                .fetch()
                .stream()
                .map(OrderSimpleQueryDto::new)
                .collect(Collectors.toList());
    }

    public List<Order> findAllWithItem() {

        return jpaQueryFactory
                .selectFrom(order)
                .distinct()
                .leftJoin(order.member, member).fetchJoin()
                .leftJoin(order.delivery, delivery).fetchJoin()
                .leftJoin(order.orderItems, orderItem).fetchJoin()
                .leftJoin(orderItem.item, item).fetchJoin()
                .fetch();

    }

    public List<OrderQueryDto> findOrderQueryDtos() {
        List<OrderQueryDto> result = findOrders();

        // N + 1
        result.forEach(o -> {
            List<OrderItemQueryDto> orderItems = findOrderItems(o.getOrderId());
            o.setOrderItems(orderItems);
        });

        return result;
    }

    public List<OrderQueryDto> findOrderQueryDtos_optimization() {
        List<OrderQueryDto> result = findOrders();

        Map<Long, List<OrderItemQueryDto>> orderItemMap = findOrderItemMap(toOrderIds(result));

        result.forEach(o -> o.setOrderItems(orderItemMap.get(o.getOrderId())));

        return result;
    }

    private List<Long> toOrderIds(List<OrderQueryDto> result) {
        return result.stream()
                .map(o -> o.getOrderId())
                .collect(Collectors.toList());
    }

    private BooleanExpression getWhereMemberNameParameter(OrderSearchDto orderSearchDto) {
        return StringUtils.hasText(orderSearchDto.getMemberName()) ? order.member.name.like(orderSearchDto.getMemberName()) : null;
    }

    private BooleanExpression getWhereOrderStatusParameter(OrderSearchDto orderSearchDto) {
        return orderSearchDto.getOrderStatus() != null ? order.status.eq(orderSearchDto.getOrderStatus()) : null;
    }

    private List<OrderQueryDto> findOrders() {
        return jpaQueryFactory
                .selectFrom(order)
                .distinct()
                .leftJoin(order.member, member).fetchJoin()
                .leftJoin(order.delivery, delivery).fetchJoin()
                .fetch()
                .stream()
                .map(o -> new OrderQueryDto(o.getId(), o.getMember().getName(), o.getOrderDate(), o.getStatus(), o.getDelivery().getAddress()))
                .collect(Collectors.toList());
    }

    private List<OrderItemQueryDto> findOrderItems(Long orderId) {
        return jpaQueryFactory
                .selectFrom(orderItem)
                .where(orderItem.order.id.eq(orderId))
                .join(orderItem.item, item).fetchJoin()
                .fetch()
                .stream()
                .map(oi -> new OrderItemQueryDto(oi.getOrder().getId(), oi.getItem().getName(), oi.getOrderPrice(), oi.getCount()))
                .collect(Collectors.toList());
    }

    //    Projections.bean(MemberDto.class, member.username, member.age)
    private Map<Long, List<OrderItemQueryDto>> findOrderItemMap(List<Long> orderIds) {
        return jpaQueryFactory
                .select(Projections
                        .constructor(
                                OrderItemQueryDto.class,
                                orderItem.order.id,
                                orderItem.item.name,
                                orderItem.orderPrice,
                                orderItem.count
                        )
                )
                .from(orderItem)
                .distinct()
                .join(orderItem.item, item)
                .where(orderItem.order.id.in(orderIds))
                .fetch()
                .stream()
                .collect(Collectors.groupingBy(OrderItemQueryDto::getOrderId));
    }
}
