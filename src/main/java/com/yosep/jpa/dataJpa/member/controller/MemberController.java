package com.yosep.jpa.dataJpa.member.controller;

import com.yosep.jpa.dataJpa.common.data.vo.Address;
import com.yosep.jpa.dataJpa.member.data.entity.User;
import com.yosep.jpa.dataJpa.member.data.entity.UserForm;
import com.yosep.jpa.dataJpa.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping(value = "/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new UserForm());
        return "members/createMemberForm";
    }

    @PostMapping(value = "/members/new")
    public String create(@Validated UserForm form, BindingResult result) {
        if(result.hasErrors()) {
            return "members/createMemberForm";
        }

        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());
        User member = new User();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);

        return "redirect:/";
    }
}
