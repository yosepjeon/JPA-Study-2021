package com.yosep.jpa.dataJpa.product.service;

import com.yosep.jpa.dataJpa.product.data.entity.Item;
import com.yosep.jpa.dataJpa.product.repository.ItemRepository;
import com.yosep.jpa.dataJpa.product.repository.ItemRepositorySupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {
    private final ItemRepository itemRepository;
    private final ItemRepositorySupport itemRepositorySupport;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public List<Item> findItems() {
        return itemRepositorySupport.findAll();
    }

    public Item findOne(long itemId) {
        return itemRepository.findOne(itemId);
    }

    @Transactional
    public void updateItem(Long id, String name, int price) {
        Item item = itemRepository.findOne(id);
        item.setName(name);
        item.setPrice(price);
    }
}
