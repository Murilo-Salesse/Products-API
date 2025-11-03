package dev.java10x.RegisterProductsAPI.Stores.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.java10x.RegisterProductsAPI.Products.DTOS.ProductIdDTO;
import dev.java10x.RegisterProductsAPI.Products.DTOS.ProductsDTO;
import dev.java10x.RegisterProductsAPI.Stores.DTOS.StoreDTO;
import dev.java10x.RegisterProductsAPI.Stores.DTOS.StoreIdDTO;
import dev.java10x.RegisterProductsAPI.Stores.DTOS.StoreWithProductsDTO;
import dev.java10x.RegisterProductsAPI.Stores.DTOS.StoreWithoutProductsDTO;
import dev.java10x.RegisterProductsAPI.Stores.Services.StoresService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StoreController.class)
public class StoreControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private StoresService storesService;

    @Autowired
    private ObjectMapper objectMapper;

    private StoreWithProductsDTO dto;
    private StoreDTO storeDTO;

    @BeforeEach
    void setUp() {
        dto = new StoreWithProductsDTO();
        dto.setId(1L);
        dto.setName("Loja Teste");
        dto.setAddress("Rua Teste");
        dto.setProducts(List.of(new ProductsDTO(1L,
                "Produto Teste",
                "Descricao Teste",
                10,
                100)));

        storeDTO = new StoreDTO(
                1L,
                "Loja Teste",
                "Rua Teste",
                List.of(new ProductIdDTO(1L))
        );
    }

    @Test
    void mustCreateStore() throws Exception{
        when(storesService.createStoreWithProducts(any())).thenReturn(storeDTO);

        mockMvc.perform(post("/api/stores")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Loja Teste"));

        verify(storesService, times(1)).createStoreWithProducts(any());
    }

    @Test
    void mustListStoresWithoutProducts() throws Exception {
        StoreWithoutProductsDTO store = new StoreWithoutProductsDTO();

        store.setId(1L);
        store.setName("Loja Teste");
        store.setAddress("Rua Teste");

        when(storesService.listStoreWithoutProducts()).thenReturn(List.of(store));

        mockMvc.perform(get("/api/stores"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Loja Teste"));
    }

    @Test
    void mustListStoresWithProducts() throws Exception {
        StoreWithProductsDTO response = new StoreWithProductsDTO(
                1L,
                "Loja Teste",
                "Rua ABC",
                List.of(new ProductsDTO(10L, "Produto X", "Descrição", 5, 99.9))
        );

        when(storesService.listStoreWithProducts()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/stores?withProducts=true"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Loja Teste"))
                .andExpect(jsonPath("$[0].products[0].name").value("Produto X"));
    }

    @Test
    void mustGetStoreById() throws Exception {
        StoreWithProductsDTO response = new StoreWithProductsDTO(
                1L,
                "Loja Teste",
                "Rua ABC",
                List.of(new ProductsDTO(10L, "Produto X", "Descrição", 5, 99.9))
        );

        when(storesService.searchStoreById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/stores/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Loja Teste"));
    }

    @Test
    void mustUpdateStore() throws Exception {
        when(storesService.updateStore(eq(1L), any())).thenReturn(dto);

        mockMvc.perform(put("/api/stores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Loja Teste"));
    }

    @Test
    void mustReturnTotalStores() throws Exception {
        long totalStores = 1L;
        when(storesService.returnTotalStores()).thenReturn(totalStores);

        mockMvc.perform(get("/api/stores/total")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.total").value(totalStores));
    }

    @Test
    void mustReturn404WhenUpdateNotFound() throws Exception {
        when(storesService.updateStore(eq(1L), any())).thenReturn(null);

        mockMvc.perform(put("/api/stores/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());
    }

    @Test
    void mustDeleteStore() throws Exception {
        doNothing().when(storesService).deleteStore(1L);

        mockMvc.perform(delete("/api/stores/1"))
                .andExpect(status().isNoContent());

        verify(storesService, times(1)).deleteStore(1L);
    }
}
