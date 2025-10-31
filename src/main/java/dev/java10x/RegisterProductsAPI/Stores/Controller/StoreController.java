package dev.java10x.RegisterProductsAPI.Stores.Controller;

import dev.java10x.RegisterProductsAPI.Stores.DTOS.*;
import dev.java10x.RegisterProductsAPI.Stores.Services.StoresService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
public class StoreController {

    private final StoresService storesService;

    public StoreController(StoresService storesService) {
        this.storesService = storesService;
    }

    @PostMapping
    public ResponseEntity<StoreDTO> createStore(@Valid @RequestBody StoreWithProductsDTO dto) {
        StoreDTO createdStore = storesService.createStoreWithProducts(dto);
        return ResponseEntity.status(201).body(createdStore);
    }

    @GetMapping
    public ResponseEntity<List<?>> listStores(
            @RequestParam(name = "withProducts", required = false) Boolean withProducts) {
        if (Boolean.TRUE.equals(withProducts)) {
            return ResponseEntity.ok(storesService.listStoreWithProducts());
        }
        return ResponseEntity.ok(storesService.listStoreWithoutProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<StoreWithProductsDTO> listStoreById(@PathVariable Long id) {
        return ResponseEntity.ok(storesService.searchStoreById(id));
    }


    @PutMapping("/{id}")
    public ResponseEntity<StoreWithProductsDTO> updateStore(
            @PathVariable Long id,
            @RequestBody StoreWithProductsDTO dto) {
        return ResponseEntity.ok(storesService.updateStore(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStore(@PathVariable Long id) {
        storesService.deleteStore(id);
        return ResponseEntity.noContent().build();
    }
}