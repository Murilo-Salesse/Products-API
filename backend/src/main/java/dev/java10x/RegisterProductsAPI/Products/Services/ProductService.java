package dev.java10x.RegisterProductsAPI.Products.Services;

import dev.java10x.RegisterProductsAPI.Stores.DTOS.StoreIdDTO;
import dev.java10x.RegisterProductsAPI.Stores.DTOS.StoreWithoutProductsDTO;
import dev.java10x.RegisterProductsAPI.Stores.Models.StoreModel;
import dev.java10x.RegisterProductsAPI.Stores.Repository.StoresRepository;
import dev.java10x.RegisterProductsAPI.Products.DTOS.ProductWithAllStoresDTO;
import dev.java10x.RegisterProductsAPI.Products.DTOS.ProductsWithStoresDTO;
import dev.java10x.RegisterProductsAPI.Products.DTOS.ProductsDTO;
import dev.java10x.RegisterProductsAPI.Products.Models.ProductModel;
import dev.java10x.RegisterProductsAPI.Products.Repository.ProductsRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductsRepository productsRepository;
    private final StoresRepository storesRepository;

    public ProductService(ProductsRepository productsRepository, StoresRepository storesRepository) {
        this.productsRepository = productsRepository;
        this.storesRepository = storesRepository;
    }

    @Transactional
    public ProductsWithStoresDTO createProductWithStore(ProductsWithStoresDTO dto) {
        ProductModel product = new ProductModel();

        product.setName(dto.getName());
        product.setValue(dto.getValue());
        product.setDescription(dto.getDescription());
        product.setQuantity(dto.getQuantity());

        List<StoreModel> stores = new ArrayList<>();
        if (dto.getStores() != null) {
            for (StoreIdDTO s : dto.getStores()) {
                StoreModel store = storesRepository.findById(s.getId())
                        .orElseThrow(() -> new RuntimeException("Loja não encontrada: " + s.getId()));
                stores.add(store);
            }
        }

        // Define a relação nos dois lados
        product.setStores(stores);
        for (StoreModel store : stores) {
            store.getProducts().add(product);
        }

        ProductModel savedProduct = productsRepository.save(product);

        List<StoreIdDTO> storesDTO = savedProduct.getStores().stream()
                .map(l -> new StoreIdDTO(l.getId()))
                .collect(Collectors.toList());  // MUDANÇA AQUI

        return new ProductsWithStoresDTO(
                savedProduct.getId(),
                savedProduct.getName(),
                savedProduct.getDescription(),
                savedProduct.getQuantity(),
                savedProduct.getValue(),
                storesDTO
        );
    }

    public List<ProductsDTO> listStoresWithoutStores(){
        return productsRepository.findAll().stream()
                .map(p -> new ProductsDTO(
                        p.getId(),
                        p.getName(),
                        p.getDescription(),
                        p.getQuantity(),
                        p.getValue()))
                .collect(Collectors.toList());  // MUDANÇA AQUI
    }

    public List<ProductWithAllStoresDTO> listProductsWithStores() {
        return productsRepository.findAll().stream()
                .map(p -> {
                    List<StoreWithoutProductsDTO> storesDTO = p.getStores().stream()
                            .map(l -> new StoreWithoutProductsDTO(
                                    l.getId(),
                                    l.getName(),
                                    l.getAddress()))
                            .collect(Collectors.toList());  // MUDANÇA AQUI
                    return new ProductWithAllStoresDTO(
                            p.getId(),
                            p.getName(),
                            p.getDescription(),
                            p.getQuantity(),
                            p.getValue(), storesDTO);
                })
                .collect(Collectors.toList());  // MUDANÇA AQUI
    }

    public ProductWithAllStoresDTO searchProductById(Long id) {
        return productsRepository.findById(id)
                .map(p -> {
                    List<StoreWithoutProductsDTO> storesDTO = p.getStores().stream()
                            .map(l -> new StoreWithoutProductsDTO(
                                    l.getId(),
                                    l.getName(),
                                    l.getAddress()))
                            .collect(Collectors.toList());  // MUDANÇA AQUI
                    return new ProductWithAllStoresDTO(
                            p.getId(),
                            p.getName(),
                            p.getDescription(),
                            p.getQuantity(),
                            p.getValue(),
                            storesDTO);
                })
                .orElseThrow(() -> new EntityNotFoundException("Produto com ID " + id + " não encontrado"));
    }

    public List<ProductsDTO> listTop5Products() {
        return productsRepository.findTop5ByOrderByValueDesc()
                .stream()
                .map(p -> new ProductsDTO(p.getId(), p.getName(), p.getDescription() ,p.getQuantity(), p.getValue()))
                .collect(Collectors.toList());
    }

    public Long returnTotalProducts() { return productsRepository.count(); }

    @Transactional
    public Optional<ProductsWithStoresDTO> updateProduct(Long id, ProductsWithStoresDTO dto) {
        ProductModel product = productsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado: " + id));

        // Atualiza os campos simples
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setQuantity(dto.getQuantity());
        product.setValue(dto.getValue());

        // Cria uma cópia da lista para evitar ConcurrentModificationException
        List<StoreModel> currentStores = new ArrayList<>(product.getStores());

        // Remove relações antigas de ambos os lados
        for (StoreModel oldStore : currentStores) {
            oldStore.getProducts().remove(product);
        }
        product.getStores().clear();

        // Adiciona novas lojas
        if (dto.getStores() != null && !dto.getStores().isEmpty()) {
            List<StoreModel> newStores = new ArrayList<>();
            for (StoreIdDTO s : dto.getStores()) {
                StoreModel store = storesRepository.findById(s.getId())
                        .orElseThrow(() -> new RuntimeException("Loja não encontrada: " + s.getId()));
                newStores.add(store);
            }

            // Define a relação nos dois lados
            product.setStores(newStores);
            for (StoreModel store : newStores) {
                if (!store.getProducts().contains(product)) {
                    store.getProducts().add(product);
                }
            }
        }

        ProductModel updated = productsRepository.save(product);

        List<StoreIdDTO> storesDTO = updated.getStores().stream()
                .map(s -> new StoreIdDTO(s.getId()))
                .collect(Collectors.toList());  // MUDANÇA AQUI

        return Optional.of(new ProductsWithStoresDTO(
                updated.getId(),
                updated.getName(),
                updated.getDescription(),
                updated.getQuantity(),
                updated.getValue(),
                storesDTO
        ));
    }

    @Transactional
    public void deleteProduct(Long id) {
        ProductModel product = productsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Produto com ID " + id + " não encontrada"));

        // Cria cópia antes de iterar
        List<StoreModel> storesCopy = new ArrayList<>(product.getStores());
        storesCopy.forEach(store -> store.getProducts().remove(product));
        product.getStores().clear();

        productsRepository.delete(product);
    }
}