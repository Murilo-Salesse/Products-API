package dev.java10x.RegisterProductsAPI.Products.Services;

import dev.java10x.RegisterProductsAPI.Products.DTOS.ProductWithAllStoresDTO;
import dev.java10x.RegisterProductsAPI.Products.DTOS.ProductsDTO;
import dev.java10x.RegisterProductsAPI.Products.DTOS.ProductsWithStoresDTO;
import dev.java10x.RegisterProductsAPI.Products.Models.ProductModel;
import dev.java10x.RegisterProductsAPI.Products.Repository.ProductsRepository;
import dev.java10x.RegisterProductsAPI.Stores.DTOS.StoreIdDTO;
import dev.java10x.RegisterProductsAPI.Stores.Models.StoreModel;
import dev.java10x.RegisterProductsAPI.Stores.Repository.StoresRepository;
import org.junit.jupiter.api.BeforeEach;
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


public class ProductServiceTest {

    @Mock
    private ProductsRepository productsRepository;

    @Mock
    private StoresRepository storesRepository;

    @InjectMocks
    private ProductService productService;

    private StoreModel store;
    private ProductsWithStoresDTO dto;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        store = new StoreModel();
        store.setId(1L);
        store.setName("Loja Teste");

        dto = new ProductsWithStoresDTO();
        dto.setName("Produto Teste");
        dto.setDescription("Descrição Teste");
        dto.setQuantity(10);
        dto.setValue(100.0);
        dto.setStores(List.of(new StoreIdDTO(1L)));
    }

    @Test
    void mustCreateProductWithStore() {
        // Arrange
       when(storesRepository.findById(1L)).thenReturn(Optional.of(store));
       when(productsRepository.save(any (ProductModel.class))).thenAnswer(inv -> {
           ProductModel saved = inv.getArgument(0);
           saved.setId(99L);
           return saved;
       });

        // Act
        ProductsWithStoresDTO result = productService.createProductWithStore(dto);

        // Assert
        assertNotNull(result);
        assertEquals("Produto Teste", result.getName());
        assertEquals(99L, result.getId());
        assertEquals(1, result.getStores().size());
        verify(productsRepository, times(1)).save(any(ProductModel.class));
    }

    @Test
    void mustShowExceptionWhenStoreNotFound() {
        //Arrange
        when(storesRepository.findById(1L)).thenReturn(Optional.empty());

        // Act + Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                productService.createProductWithStore(dto));

        assertEquals("Loja não encontrada", ex.getMessage());
        verify(productsRepository, never()).save(any());
    }

    @Test
    void mustReturnProductsWithoutStores() {
        //Arrange
        ProductModel p1 = new ProductModel();
        p1.setId(1L);
        p1.setName("Produto 1");

        when(productsRepository.findAll()).thenReturn(List.of(p1));

        // Act
        List<ProductsDTO> result = productService.listStoresWithoutStores();

        // Assert
        assertEquals(1, result.size());
        assertEquals("Produto 1", result.get(0).getName());
    }

    @Test
    void mustReturnEmptyListWhenHaveNoProducts() {
        // Arrange
        when(productsRepository.findAll()).thenReturn(List.of());

        // Act
        List<ProductsDTO> result = productService.listStoresWithoutStores();

        // Assert
        assertTrue(result.isEmpty());
    }

    @Test
    void mustReturnProductsWithList() {
        // Arrange
        StoreModel store = new StoreModel();
        store.setId(3L);
        store.setName("Loja teste");
        store.setAddress("Endereço teste");

        ProductModel product = new ProductModel();
        product.setId(1L);
        product.setName("Produto com Loja");
        product.setStores(List.of(store));


        when(productsRepository.findAll()).thenReturn(List.of(product));

        // Act
        List<ProductWithAllStoresDTO> result = productService.listProductsWithStores();

        // Assert
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getStores().size());
    }

    @Test
    void mustSearchProductById() {
        // Arrange
        StoreModel store = new StoreModel();
        store.setId(1L);

        ProductModel product = new ProductModel();
        product.setId(1L);
        product.setName("Produto Teste");
        product.setStores(List.of(store));

        when(productsRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act
        ProductWithAllStoresDTO result = productService.searchProductById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Produto Teste", result.getName());
    }

    @Test
    void mustUpdateProduct() {
        // Arrange
        StoreModel store = new StoreModel();
        store.setId(1L);
        store.setProducts(new ArrayList<>());

        ProductModel existingProduct = new ProductModel();
        existingProduct.setId(1L);
        existingProduct.setStores(new ArrayList<>());

        ProductsWithStoresDTO dto = new ProductsWithStoresDTO();
        dto.setName("Produto Atualizado");
        dto.setStores(List.of(new StoreIdDTO(1L)));

        when(productsRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(storesRepository.findById(1L)).thenReturn(Optional.of(store));
        when(productsRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        // Act
        Optional<ProductsWithStoresDTO> result = productService.updateProduct(1L, dto);

        // Assert
        assertTrue(result.isPresent());
        assertEquals("Produto Atualizado", result.get().getName());
    }

    @Test
    void mustDeleteProduct() {
        // Arrange
        StoreModel store = new StoreModel();
        store.setProducts(new ArrayList<>());

        ProductModel product = new ProductModel();
        product.setId(1L);
        product.setStores(new ArrayList<>(List.of(store)));

        store.getProducts().add(product);

        when(productsRepository.findById(1L)).thenReturn(Optional.of(product));

        // Act
        productService.deleteProduct(1L);

        // Assert
        verify(productsRepository, times(1)).delete(product);
        assertTrue(product.getStores().isEmpty());
    }
}
