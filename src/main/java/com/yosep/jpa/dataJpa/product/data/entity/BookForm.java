package com.yosep.jpa.dataJpa.product.data.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookForm {
    private long id;
    private String name;
    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;
}
