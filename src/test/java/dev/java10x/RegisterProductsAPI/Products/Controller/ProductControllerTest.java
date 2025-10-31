package dev.java10x.RegisterProductsAPI.Products.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.java10x.RegisterProductsAPI.Products.DTOS.ProductWithAllStoresDTO;
import dev.java10x.RegisterProductsAPI.Products.DTOS.ProductsDTO;
import dev.java10x.RegisterProductsAPI.Products.DTOS.ProductsWithStoresDTO;
import dev.java10x.RegisterProductsAPI.Products.Services.ProductService;
import dev.java10x.RegisterProductsAPI.Stores.DTOS.StoreIdDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;


import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductsWithStoresDTO dto;

    @BeforeEach
    void setUp() {
        dto = new ProductsWithStoresDTO();

        dto.setId(1L);
        dto.setName("Produto Teste");
        dto.setDescription("Descrição");
        dto.setQuantity(10);
        dto.setValue(99.9);
        dto.setStores(List.of(new StoreIdDTO(1L)));
    }

    @Test
    void mustCreateProduct() throws Exception {
        when(productService.createProductWithStore(any())).thenReturn(dto);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Produto Teste"));

        verify(productService, times(1)).createProductWithStore(any());
    }

    @Test
    void mustListProductsWithoutStores() throws Exception {
       ProductsDTO product = new ProductsDTO();

        product.setId(1L);
        product.setName("Produto Teste");
        product.setDescription("Descrição");
        product.setQuantity(10);
        product.setValue(99.9);

        when(productService.listStoresWithoutStores()).thenReturn(List.of(product));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Produto Teste"));
    }

    @Test
    void mustListProductsWithStores() throws Exception {
        ProductWithAllStoresDTO response = new ProductWithAllStoresDTO(
                1L, "Produto Completo", "Desc", 5, 50.0, List.of());

        when(productService.listProductsWithStores()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/products?withStores=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Produto Completo"));
    }

    @Test
    void mustGetProductById() throws Exception {
        ProductWithAllStoresDTO response = new ProductWithAllStoresDTO(
                1L, "Produto ID", "Desc", 5, 50.0, List.of());

        when(productService.searchProductById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/products/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Produto ID"));
    }

    @Test
    void mustUpdateProduct() throws Exception {
        when(productService.updateProduct(eq(1L), any())).thenReturn(Optional.of(dto));

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Produto Teste"));
    }

    @Test
    void mustReturn404WhenUpdateNotFound() throws Exception {
        when(productService.updateProduct(eq(1L), any())).thenReturn(Optional.empty());

        mockMvc.perform(put("/api/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNotFound());
    }

    @Test
    void mustDeleteProduct() throws Exception {
        doNothing().when(productService).deleteProduct(1L);

        mockMvc.perform(delete("/api/products/1"))
                .andExpect(status().isNoContent());

        verify(productService, times(1)).deleteProduct(1L);
    }
}