package com.yosep.jpa.dataJpa.order.controller;

import com.yosep.jpa.dataJpa.member.data.entity.Member;
import com.yosep.jpa.dataJpa.member.service.MemberService;
import com.yosep.jpa.dataJpa.order.data.dto.OrderSearchDto;
import com.yosep.jpa.dataJpa.order.data.entity.Order;
import com.yosep.jpa.dataJpa.order.service.OrderService;
import com.yosep.jpa.dataJpa.product.data.entity.Item;
import com.yosep.jpa.dataJpa.product.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping(value = "/order")
    public String createForm(Model model) {
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "order/orderForm";
    }

    @PostMapping(value = "/order")
    public String order(@RequestParam("memberId") Long memberId,
                        @RequestParam("itemId") Long itemId, @RequestParam("count") int count) {
        orderService.order(memberId, itemId, count);
        return "redirect:/orders";
    }

    @GetMapping(value = "/orders")
    public String orderList(@ModelAttribute("orderSearch")OrderSearchDto orderSearchDto, Model model) {
        List<Order> orders = orderService.findOrders(orderSearchDto);
        model.addAttribute("orders", orders);

        return "order/orderList";
    }
}
