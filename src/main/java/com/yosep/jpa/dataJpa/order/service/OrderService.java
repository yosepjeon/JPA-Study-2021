package com.yosep.jpa.dataJpa.order.service;

import com.yosep.jpa.dataJpa.common.data.entity.Delivery;
import com.yosep.jpa.dataJpa.common.data.vo.DeliveryStatus;
import com.yosep.jpa.dataJpa.member.data.entity.User;
import com.yosep.jpa.dataJpa.member.repository.MemberRepository;
import com.yosep.jpa.dataJpa.order.data.entity.Order;
import com.yosep.jpa.dataJpa.order.data.entity.OrderItem;
import com.yosep.jpa.dataJpa.order.repository.OrderRepository;
import com.yosep.jpa.dataJpa.product.data.entity.Item;
import com.yosep.jpa.dataJpa.product.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public long order(Long memberId, long itemId, int count) {
        User member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
        Order order = Order.createOrder(member, delivery, orderItem);

        orderRepository.save(order);
        return order.getId();

    }

    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findOne(orderId);

        order.cancel();
    }
}
