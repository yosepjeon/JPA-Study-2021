package com.yosep.jpa.dataJpa.member.data.entity;

import com.yosep.jpa.dataJpa.common.data.entity.Order;
import com.yosep.jpa.dataJpa.common.data.vo.Address;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String name;

    @Embedded
    private Address address;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();
}