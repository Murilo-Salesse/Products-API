package dev.java10x.RegisterProductsAPI.Stores.Services;

import dev.java10x.RegisterProductsAPI.Products.DTOS.ProductsDTO;
import dev.java10x.RegisterProductsAPI.Products.Models.ProductModel;
import dev.java10x.RegisterProductsAPI.Products.Repository.ProductsRepository;
import dev.java10x.RegisterProductsAPI.Stores.DTOS.StoreDTO;
import dev.java10x.RegisterProductsAPI.Stores.DTOS.StoreWithProductsDTO;
import dev.java10x.RegisterProductsAPI.Stores.DTOS.StoreWithoutProductsDTO;
import dev.java10x.RegisterProductsAPI.Stores.Models.StoreModel;
import dev.java10x.RegisterProductsAPI.Stores.Repository.StoresRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class StoreServiceTest {

    @Mock
    private StoresRepository storesRepository;

    @Mock
    private ProductsRepository productsRepository;

    @InjectMocks
    private StoresService storesService;

    private ProductModel product;
    private StoreWithProductsDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        product = new ProductModel();
        product.setId(1L);
        product.setName("Produto Teste");

        dto = new StoreWithProductsDTO();
        dto.setName("Loja Teste");
        dto.setAddress("Endereço Teste");
        dto.setProducts(List.of(new ProductsDTO(
                1L,
                "Produto Teste",
                "Descricao Teste",
                1,
                10)));
    }

    @Test
    @DisplayName("Must create a store with products")
    void mustCreateStoreWithProduct() {
        //Arrange
        when(productsRepository.findById(1L)).thenReturn(Optional.of(product));
        when(storesRepository.save(any(StoreModel.class))).thenAnswer(inv -> {
            StoreModel saved = inv.getArgument(0);
            saved.setId(100L);
            return saved;
        });

        // Act
        StoreDTO result = storesService.createStoreWithProducts(dto);

        // Assert
        assertNotNull(result);
        assertEquals(100L, result.getId());
        assertEquals("Loja Teste", result.getName());
        assertEquals("Endereço Teste", result.getAddress());

        assertNotNull(result.getProducts());
        assertEquals(1, result.getProducts().size());
        assertEquals(1L, result.getProducts().get(0).getId());

        verify(storesRepository, times(1)).save(any(StoreModel.class));
    }

    @Test
    @DisplayName("Must show exception when not found a store")
    void mustShowExceptionWhenProductNotFound() {
        // Arrange
        when(productsRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        RuntimeException ex = assertThrows(EntityNotFoundException.class, () ->
                storesService.createStoreWithProducts(dto));

        assertEquals("Produto não encontrado: 1", ex.getMessage());
        verify(storesRepository, never()).save(any());
    }

    @Test
    @DisplayName("Must return stores without products")
    void mustReturnStoresWithoutProducts() {
        // Arrange
        StoreModel s1 = new StoreModel();
        s1.setId(1L);
        s1.setName("Loja 1");
        s1.setAddress("Rua Teste");

        when(storesRepository.findAll()).thenReturn(List.of(s1));

        // Act
        List<StoreWithoutProductsDTO> result = storesService.listStoreWithoutProducts();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Loja 1", result.get(0).getName());
        assertEquals("Rua Teste", result.get(0).getAddress());
    }

    @Test
    @DisplayName("Must return empty list when dont have stores")
    void mustReturnEmptyListWhenHaveNoStores() {
        // Arrange
        when(storesRepository.findAll()).thenReturn(List.of());

        // Act
        List<StoreWithoutProductsDTO> result = storesService.listStoreWithoutProducts();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Must return store with products")
    void mustReturnStoresWithList() {
        // Arrange
        StoreModel store = new StoreModel();
        store.setId(1L);
        store.setName("Loja teste");
        store.setAddress("Endereço teste");

        ProductModel product = new ProductModel();
        product.setId(1L);
        product.setName("Produto com Loja");
        product.setStores(List.of(store));

        store.setProducts(List.of(product));

        when(storesRepository.findAll()).thenReturn(List.of(store));

        // Act
        List<StoreWithProductsDTO> result = storesService.listStoreWithProducts();

        // Assert
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getProducts().size());
    }

    @Test
    @DisplayName("Must return total of stores")
    void mustReturnTotalStores() {
        //Arrange
        StoreModel store = new StoreModel();
        store.setId(1L);

        when(storesRepository.count()).thenReturn(1L);

        // Act
        Long total = storesService.returnTotalStores();

        // Assert
        assertEquals(1, total);
    }

    @Test
    @DisplayName("Must search a store by ID")
    void mustSearchByStoreId(){
        // Arrange
        StoreModel store = new StoreModel();
        store.setId(1L);
        store.setName("Loja Teste");
        store.setAddress("Rua Teste");

        ProductModel product = new ProductModel();
        product.setId(1L);
        product.setName("Produto Teste");
        product.setStores(List.of(store));


        when(storesRepository.findById(1L)).thenReturn(Optional.of(store));

        // Act
        StoreWithProductsDTO result = storesService.searchStoreById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Loja Teste", result.getName());
        assertEquals("Rua Teste", result.getAddress());
    }

    @Test
    @DisplayName("Must update a store")
    void mustUpdateStore(){
        // Arrange
        StoreModel store = new StoreModel();
        store.setId(1L);
        store.setProducts(new ArrayList<>());

        ProductModel existingProduct = new ProductModel();
        existingProduct.setId(1L);
        existingProduct.setStores(new ArrayList<>());

        StoreWithProductsDTO dto = new StoreWithProductsDTO();
        dto.setName("Loja Atualizada");
        dto.setAddress("Rua Teste");
        dto.setProducts(List.of(new ProductsDTO(
                1L,
                "Produto Teste",
                "Descricao Teste",
                1,
                10)));

        when(storesRepository.findById(1L)).thenReturn(Optional.of(store));
        when(productsRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(storesRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Optional<StoreWithProductsDTO> result = Optional.ofNullable(storesService.updateStore(1L, dto));

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Loja Atualizada", result.get().getName());
    }

    @Test
    @DisplayName("Must delete a store")
    void mustDeleteStore(){
        // Arrange
        StoreModel store = new StoreModel();
        store.setId(1L);
        store.setProducts(new ArrayList<>());

        ProductModel product = new ProductModel();
        product.setId(1L);
        product.setStores(new ArrayList<>(List.of(store)));

        store.getProducts().add(product);

        when(storesRepository.findById(1L)).thenReturn(Optional.of(store));

        // Act
        storesService.deleteStore(1L);

        // Assert
        verify(storesRepository, times(1)).delete(store);
        assertTrue(store.getProducts().isEmpty());
    }
}
