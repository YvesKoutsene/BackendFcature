package glsi.example.backendfacture.service;

import glsi.example.backendfacture.model.ProductModel;
import glsi.example.backendfacture.repository.ProductRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DataInitializationService {

    @Autowired
    private ProductRepository productRepository;

    @PostConstruct
    public void initializeData() {
        if (productRepository.count() == 0) {
            insertProduct("Smartphone", 599.99);
            insertProduct("Laptop", 1299.99);
            insertProduct("Smartwatch", 199.99);
            insertProduct("Wireless Earbuds", 79.99);
            insertProduct("Fitness Tracker", 49.99);
            insertProduct("Bluetooth Speaker", 89.99);
            insertProduct("Camera", 799.99);
            insertProduct("4K TV", 1499.99);
            insertProduct("Gaming Console", 399.99);
            insertProduct("Tablet", 449.99);
            insertProduct("Headphones", 129.99);
            insertProduct("Drone", 499.99);
            insertProduct("Coffee Maker", 59.99);
            insertProduct("Instant Pot", 129.99);
            insertProduct("Electric Toothbrush", 39.99);
        }
    }

    private void insertProduct(String name, double price) {
        ProductModel product = new ProductModel();
        product.setNameProduct(name);
        product.setPriceUnit(price);
        productRepository.save(product);
    }
}
