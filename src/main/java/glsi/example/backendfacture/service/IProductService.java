package glsi.example.backendfacture.service;

import glsi.example.backendfacture.model.ProductModel;

import java.util.List;

public interface IProductService {

    List<ProductModel> getAllProduits();
    ProductModel getProduitById(int productId);
    ProductModel createProduit(ProductModel product);
    ProductModel updateProduit(int productId, ProductModel updatedProduct);
    void deleteProduit(int productId);

}
