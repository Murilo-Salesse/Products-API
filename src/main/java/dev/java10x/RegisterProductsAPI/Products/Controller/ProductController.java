package dev.java10x.RegisterProductsAPI.Products.Controller;

import dev.java10x.RegisterProductsAPI.Products.DTOS.ProductWithAllStoresDTO;
import dev.java10x.RegisterProductsAPI.Products.DTOS.ProductsWithStoresDTO;
import dev.java10x.RegisterProductsAPI.Products.Services.ProductService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductsWithStoresDTO> createProduct(@RequestBody ProductsWithStoresDTO products) {
        ProductsWithStoresDTO createProduct = productService.createProductWithStore(products);
        return ResponseEntity.status(201).body(createProduct);
    }

    @GetMapping
    public ResponseEntity<List<?>> listProducts(
            @RequestParam(name = "withStores", required = false) Boolean withStores) {
        if (Boolean.TRUE.equals(withStores)) {
            return ResponseEntity.ok(productService.listProductsWithStores());
        }
        return ResponseEntity.ok(productService.listStoresWithoutStores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductWithAllStoresDTO> listProductWithId(@PathVariable Long id) {
        return productService.searchProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductsWithStoresDTO> updateProduct(
            @PathVariable Long id, @RequestBody ProductsWithStoresDTO dto) {
        return productService.updateProduct(id, dto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        if (productService.deleteProduct(id)) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

}
