package com.yosep.jpa.dataJpa.order.data.dto;

import com.yosep.jpa.dataJpa.order.data.vo.OrderStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderSearchDto {
    private String memberName;
    private OrderStatus orderStatus;
}
