package org.moduleap.api.controllers;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("item-api")
public interface ItemClient {
    @PostMapping("/item")
    ResponseEntity<?> createItem();
}
