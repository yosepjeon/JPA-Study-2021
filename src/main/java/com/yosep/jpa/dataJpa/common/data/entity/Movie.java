package com.yosep.jpa.dataJpa.common.data.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("M")
@Getter
@Setter
public class Movie extends Item{
    private String director;
    private String actor;
}
