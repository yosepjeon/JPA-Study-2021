package com.yosep.jpa.dataJpa.order.data.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.yosep.jpa.dataJpa.common.data.vo.Address;
import com.yosep.jpa.dataJpa.order.data.vo.OrderStatus;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class OrderItemQueryDto {
    @JsonIgnore
    private Long orderId;
    private String itemName;
    private int orderPrice;
    private int count;

    public OrderItemQueryDto(Long orderId, String itemName, int orderPrice, int count) {
        this.orderId = orderId;
        this.itemName = itemName;
        this.orderPrice = orderPrice;
        this.count = count;
    }
}
