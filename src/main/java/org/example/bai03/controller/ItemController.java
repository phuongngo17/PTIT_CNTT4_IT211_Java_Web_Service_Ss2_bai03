package org.example.bai03.controller;

import org.example.bai03.model.Item;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/api/v1/items")
public class ItemController {

    private List<Item> items = new ArrayList<>();
    private AtomicLong nextId = new AtomicLong(1);

    @GetMapping(
            value = "/{id}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public ResponseEntity<Item> getItemById(
            @PathVariable Long id) {

        Optional<Item> item = items.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();

        return item.map(value ->
                        new ResponseEntity<>(value, HttpStatus.OK))
                .orElseGet(() ->
                        new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping(
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public ResponseEntity<Item> createItem(
            @RequestBody Item item) {

        item.setId(nextId.getAndIncrement());

        items.add(item);

        return new ResponseEntity<>(
                item,
                HttpStatus.CREATED
        );
    }

    @PutMapping(
            value = "/{id}",
            produces = {
                    MediaType.APPLICATION_JSON_VALUE,
                    MediaType.APPLICATION_XML_VALUE
            }
    )
    public ResponseEntity<Item> updateItem(
            @PathVariable Long id,
            @RequestBody Item updatedItem) {

        Optional<Item> existingItem = items.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();

        if (existingItem.isPresent()) {

            Item item = existingItem.get();

            item.setName(updatedItem.getName());
            item.setQuantity(updatedItem.getQuantity());

            return new ResponseEntity<>(
                    item,
                    HttpStatus.OK
            );
        }

        return new ResponseEntity<>(
                HttpStatus.NOT_FOUND
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteItem(
            @PathVariable Long id) {

        Optional<Item> existingItem = items.stream()
                .filter(i -> i.getId().equals(id))
                .findFirst();

        if (existingItem.isPresent()) {

            items.remove(existingItem.get());

            return new ResponseEntity<>(
                    HttpStatus.NO_CONTENT
            );
        }

        return new ResponseEntity<>(
                HttpStatus.NOT_FOUND
        );
    }
}