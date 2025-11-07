package dev.java10x.RegisterProductsAPI.Stores.Services;

import dev.java10x.RegisterProductsAPI.Stores.DTOS.*;
import dev.java10x.RegisterProductsAPI.Stores.Models.StoreModel;
import dev.java10x.RegisterProductsAPI.Stores.Repository.StoresRepository;
import dev.java10x.RegisterProductsAPI.Products.DTOS.ProductIdDTO;
import dev.java10x.RegisterProductsAPI.Products.DTOS.ProductsDTO;
import dev.java10x.RegisterProductsAPI.Products.Models.ProductModel;
import dev.java10x.RegisterProductsAPI.Products.Repository.ProductsRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoresService {

    private final StoresRepository storesRepository;
    private final ProductsRepository productsRepository;

    public StoresService(StoresRepository storesRepository, ProductsRepository productsRepository) {
        this.storesRepository = storesRepository;
        this.productsRepository = productsRepository;
    }

    @Transactional
    public StoreDTO createStoreWithProducts(StoreWithProductsDTO dto) {
        StoreModel store = new StoreModel();

        store.setName(dto.getName());
        store.setAddress(dto.getAddress());

        List<ProductModel> products = dto.getProducts() != null
                ? new ArrayList<>(dto.getProducts().stream()
                .map(p -> productsRepository.findById(p.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + p.getId())))
                .collect(Collectors.toList()))  // MUDANÇA AQUI
                : new ArrayList<>();

        store.setProducts(products);
        products.forEach(produto -> produto.getStores().add(store));

        StoreModel savedStore = storesRepository.save(store);

        List<ProductIdDTO> produtosDTO = savedStore.getProducts().stream()
                .map(p -> new ProductIdDTO(p.getId()))
                .collect(Collectors.toList());  // MUDANÇA AQUI

        return new StoreDTO(
                savedStore.getId(),
                savedStore.getName(),
                savedStore.getAddress(),
                produtosDTO);
    }

    public List<StoreWithoutProductsDTO> listStoreWithoutProducts() {
        return storesRepository.findAll().stream()
                .map(l -> new StoreWithoutProductsDTO(l.getId(),
                        l.getName(),
                        l.getAddress()))
                .collect(Collectors.toList());  // MUDANÇA AQUI
    }

    public List<StoreWithProductsDTO> listStoreWithProducts() {
        return storesRepository.findAll().stream()
                .map(l -> {
                    List<ProductsDTO> productsDTO = l.getProducts().stream()
                            .map(p -> new ProductsDTO(p.getId(),
                                    p.getName(),
                                    p.getDescription(),
                                    p.getQuantity(),
                                    p.getValue()))
                            .collect(Collectors.toList());  // MUDANÇA AQUI
                    return new StoreWithProductsDTO(l.getId(), l.getName(), l.getAddress(), productsDTO);
                })
                .collect(Collectors.toList());  // MUDANÇA AQUI
    }

    public StoreWithProductsDTO searchStoreById(long id) {
        StoreModel store = storesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Loja com ID " + id + " não encontrada"));

        List<ProductsDTO> productsDTO = store.getProducts().stream()
                .map(p -> new ProductsDTO(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getQuantity(),
                        p.getValue()))
                .collect(Collectors.toList());  // MUDANÇA AQUI

        return new StoreWithProductsDTO(
                store.getId(),
                store.getName(),
                store.getAddress(),
                productsDTO);
    }

    public Long returnTotalStores() {
        return storesRepository.count();
    }

    @Transactional
    public StoreWithProductsDTO updateStore(long id, StoreWithProductsDTO dto) {
        StoreModel store = storesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Loja com ID " + id + " não encontrada"));

        store.setName(dto.getName());
        store.setAddress(dto.getAddress());

        // Busca os novos produtos e cria uma lista mutável
        List<ProductModel> newProducts = dto.getProducts() != null
                ? new ArrayList<>(dto.getProducts().stream()
                .map(p -> productsRepository.findById(p.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + p.getId())))
                .collect(Collectors.toList()))  // MUDANÇA AQUI
                : new ArrayList<>();

        // Cria cópia da lista atual antes de modificar
        List<ProductModel> currentProducts = new ArrayList<>(store.getProducts());

        // Remove relações antigas
        currentProducts.forEach(prod -> prod.getStores().remove(store));
        store.getProducts().clear();

        // Adiciona novas relações
        newProducts.forEach(prod -> {
            if (!prod.getStores().contains(store)) {
                prod.getStores().add(store);
            }
        });

        store.setProducts(newProducts);

        StoreModel updatedStore = storesRepository.save(store);

        List<ProductsDTO> productsDTO = updatedStore.getProducts().stream()
                .map(p -> new ProductsDTO(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getQuantity(),
                        p.getValue()))
                .collect(Collectors.toList());  // MUDANÇA AQUI

        return new StoreWithProductsDTO(
                updatedStore.getId(),
                updatedStore.getName(),
                updatedStore.getAddress(),
                productsDTO);
    }

    @Transactional
    public void deleteStore(long id) {
        StoreModel store = storesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Loja com ID " + id + " não encontrada"));

        // Cria cópia antes de iterar
        List<ProductModel> productsCopy = new ArrayList<>(store.getProducts());
        productsCopy.forEach(prod -> prod.getStores().remove(store));
        store.getProducts().clear();

        storesRepository.delete(store);
    }
}