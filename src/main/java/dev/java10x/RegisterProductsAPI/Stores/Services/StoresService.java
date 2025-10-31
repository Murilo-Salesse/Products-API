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
import java.util.Optional;

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
                .toList())
                : new ArrayList<>();

        store.setProducts(products);
        products.forEach(produto -> produto.getStores().add(store));

        StoreModel savedStore = storesRepository.save(store);

        List<ProductIdDTO> produtosDTO = savedStore.getProducts().stream()
                .map(p -> new ProductIdDTO(p.getId()))
                .toList();

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
                .toList();
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
                            .toList();
                    return new StoreWithProductsDTO(l.getId(), l.getName(), l.getAddress(), productsDTO);
                })
                .toList();
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
                .toList();

        return new StoreWithProductsDTO(
                store.getId(),
                store.getName(),
                store.getAddress(),
                productsDTO);
    }

    @Transactional
    public StoreWithProductsDTO updateStore(long id, StoreWithProductsDTO dto) {
        StoreModel store = storesRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Loja com ID " + id + " não encontrada"));

        store.setName(dto.getName());
        store.setAddress(dto.getAddress());

        List<ProductModel> newProducts = dto.getProducts() != null
                ? dto.getProducts().stream()
                .map(p -> productsRepository.findById(p.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado: " + p.getId())))
                .toList()
                : new ArrayList<>();

        store.getProducts().forEach(prod -> prod.getStores().remove(store));
        newProducts.forEach(prod -> prod.getStores().add(store));

        store.setProducts(newProducts);

        StoreModel updatedStore = storesRepository.save(store);

        List<ProductsDTO> productsDTO = updatedStore.getProducts().stream()
                .map(p -> new ProductsDTO(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getQuantity(),
                        p.getValue()))
                .toList();

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

        store.getProducts().forEach(prod -> prod.getStores().remove(store));
        store.getProducts().clear();

        storesRepository.delete(store);
    }
}